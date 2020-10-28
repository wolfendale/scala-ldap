package wolfendale

import scodec._
import scodec.bits._
import scodec.codecs._

package object ber {

  def tagged[A](identifier: Identifier)(codec: Codec[A]): Codec[A] =
    Identifier.codec.unit(identifier) ~> new BerContentSizeCodec(codec)

  def tagged[A](tagClass: TagClass, constructionType: ConstructionType, tagNumber: Int)(codec: Codec[A]): Codec[A] =
    tagged(Identifier(tagClass, constructionType, tagNumber))(codec)

  def sized[A](codec: Codec[A]): Codec[A] =
    new BerContentSizeCodec[A](codec)

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
    tagged(TagClass.Universal, ConstructionType.Primitive, 2)(BerIntegerCodec)

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

  def octetString[A](
                      codec: Codec[A],
                      identifier: Identifier = Identifier(TagClass.Universal, ConstructionType.Primitive, 4)
                    ): Codec[A] = tagged(identifier)(byteAligned(codec))

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
  def sequenceOf[A](
                     codec: Codec[A],
                     identifier: Identifier = Identifier(TagClass.Universal, ConstructionType.Constructed, 16)
                   ): Codec[Vector[A]] = tagged(identifier)(vector(codec))

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
    tagged(TagClass.Universal, ConstructionType.Primitive, 10)(BerIntegerCodec.xmap(g, f))
}
