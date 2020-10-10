package wolfendale

import scodec._
import scodec.bits._
import scodec.codecs._

import scala.annotation.tailrec
import scala.util.Try

package object ber {

  private val tagNumberCodec: Codec[Int] = {
    val longTag: Codec[Int] = (constant(bin"11111") ~ BerTagLengthCodec).exmap(
      { case (_, int)        => Attempt.successful(int) },
      { case int if int > 30 => Attempt.successful(() -> int)
      case _               => Attempt.failure(Err("Invalid tag type length"))
      }
    )
    choice(longTag, uint(5))
  }

  def tagged[A](tagClass: TagClass, constructionType: ConstructionType, tagNumber: Int)(codec: Codec[A]): Codec[A] =
    TagClass.codec.unit(tagClass) ~>
    ConstructionType.codec.unit(constructionType) ~>
    tagNumberCodec.unit(tagNumber) ~>
    new BerContentSizeCodec(codec)

  val boolean: Codec[Boolean] =
    tagged(TagClass.Universal, ConstructionType.Primitive, 1) {
      uint8.xmap(
        { case 0 => false
          case _ => true
        }, {
          case true  => 1
          case false => 0
        }
      )
    }

  val integer: Codec[Int] =
    tagged(TagClass.Universal, ConstructionType.Primitive, 2) {
      new Codec[Int] {

        private val highPrefix = BitVector.high(9)
        private val lowPrefix = BitVector.low(9)

        @tailrec
        private def shrink(bits: BitVector): BitVector =
          if (bits.startsWith(highPrefix) || bits.startsWith(lowPrefix)) shrink(bits.drop(8)) else bits

        override val sizeBound: SizeBound = SizeBound.bounded(8, 4 * 8)

        override def encode(value: Int): Attempt[BitVector] =
          Attempt.successful(shrink(BitVector.fromInt(value)))

        override def decode(bits: BitVector): Attempt[DecodeResult[Int]] =
          Attempt.fromTry(Try(bits.toInt(signed = true))).map(DecodeResult(_, BitVector.empty))
      }
    }

  def bitString[A](codec: Codec[A]): Codec[A] =
    tagged(TagClass.Universal, ConstructionType.Primitive, 3)(new Codec[A] {

      private val decoder = ignore(8) ~> codec

      override val sizeBound: SizeBound = SizeBound.atLeast(codec.sizeBound.lowerBound + 8)

      override def encode(value: A): Attempt[BitVector] = for {
        bits                 <- codec.encode(value)
        numberOfTrailingBits =  ((8 - bits.length % 8) % 8).toInt
        prefix               <- uint8.encode(numberOfTrailingBits)
      } yield prefix ++ bits ++ BitVector.low(numberOfTrailingBits)

      override def decode(bits: BitVector): Attempt[DecodeResult[A]] =
        decoder.decode(bits)
    })

  def octetString[A](codec: Codec[A]): Codec[A] =
    tagged(TagClass.Universal, ConstructionType.Primitive, 4)(byteAligned(codec))

  def berNull: Codec[Unit] =
    tagged(TagClass.Universal, ConstructionType.Primitive, 5)(provide(()))

  /**
   * In order to be compliant with X.690, the codec
   * param must encode a BER encoded TLV
   *
   * @param codec
   * @tparam A
   * @return
   */
  def sequence[A](codec: Codec[A]): Codec[A] =
    tagged(TagClass.Universal, ConstructionType.Constructed, 16)(codec)

  /**
   * In order to be compliant with X.690, the codec
   * param must encode a BER encoded TLV
   *
   * @param codec
   * @tparam A
   * @return
   */
  def sequenceOf[A](codec: Codec[A]): Codec[Vector[A]] =
    tagged(TagClass.Universal, ConstructionType.Constructed, 16)(vector(codec))


  /**
   * In order to be compliant with X.690, the codec
   * param must encode a BER encoded TLV
   *
   * NOTE: this does not enforce the semantics
   * of a BER set, that is down to the construction
   * of the underlying codec
   *
   * @param codec
   * @tparam A
   * @return
   */
  def set[A](codec: Codec[A]): Codec[A] =
    tagged(TagClass.Universal, ConstructionType.Constructed, 17)(codec)

  /**
   * In order to be compliant with X.690, the codec
   * param must encode a BER encoded TLV
   *
   * @param codec
   * @tparam A
   * @return
   */
  def setOf[A](codec: Codec[A]): Codec[Set[A]] =
    tagged(TagClass.Universal, ConstructionType.Constructed, 17)(vector(codec)).xmap(
      { _.toSet },
      { _.toVector }
    )

  def enumerated[A](f: A => Int, g: Int => A): Codec[A] =
    integer.xmap(g, f)
}
