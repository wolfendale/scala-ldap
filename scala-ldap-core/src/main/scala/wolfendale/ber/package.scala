package wolfendale

import scodec._
import scodec.bits._
import scodec.codecs._

import scala.annotation.tailrec

package object ber {

  val berInt: Codec[Int] = new Codec[Int] {

    override def sizeBound: SizeBound = SizeBound.bounded(8, 8 * 5)

    override def encode(value: Int): Attempt[BitVector] =
      runEncode(BitVector.empty, value)

    override def decode(bits: BitVector): Attempt[DecodeResult[Int]] =
      runDecode(bits, 0)

    @tailrec
    private def runDecode(buffer: BitVector, value: Int): Attempt[DecodeResult[Int]] = {
      val result = buffer.sliceToInt(1, 7, signed = false) + (value << 7)
      if (buffer.get(0)) {
        runDecode(buffer.drop(8), result)
      } else Attempt.successful {
        DecodeResult(result, buffer.drop(8))
      }
    }

    /* 10000000 */
    private val MoreOctets = 128

    @tailrec
    private def runEncode(buffer: BitVector, value: Int): Attempt[BitVector] = {
      if (buffer.isEmpty) {
        runEncode(BitVector.fromInt(value & ~MoreOctets, 8), value >> 7)
      } else if (Integer.compareUnsigned(value, 0) > 0) {
        runEncode(buffer.splice(0, BitVector.fromInt(value | MoreOctets, 8)), value >> 7)
      } else Attempt.successful(buffer)
    }
  }

  private def sized[A](codec: Codec[A]): Codec[A] = new Codec[A] {

    private def efficientSize(long: Long): Int = {
      @tailrec
      def inner(long: Long, size: Int): Int = {
        val next = long >> 8
        if (next > 0) inner(next, size + 1) else size
      }
      inner(long, 1)
    }

    private val lengthCodec: Codec[Long] = {
      bool.consume {
        case false => ulong(7)
        case true  => ulong(7).consume {
          case 0                 => fail[Long](Err("Content of indefinite length is not supported"))
          case size if size <= 8 => ulong(size.toInt * 8)
          case 127               => fail[Long](Err("Initial length value of 0xFF is reserved"))
          case _                 => fail[Long](Err("Content lengths that are greater than (2 ^ 64) bytes are not supported"))
        } (efficientSize)
      } (java.lang.Long.compareUnsigned(_, 127L) > 0)
    }

    private val decoder: Decoder[A] =
      lengthCodec.flatMap(fixedSizeBytes(_, codec))

    override def sizeBound: SizeBound = SizeBound.atLeast(8)

    override def encode(value: A): Attempt[BitVector] = for {
      content <- codec.encode(value)
      size    <- lengthCodec.encode(content.size / 8)
    } yield size ++ content

    override def decode(bits: BitVector): Attempt[DecodeResult[A]] =
      decoder.decode(bits)
  }

  def ber[A](identifier: Identifier, codec: Codec[A]): Codec[A] =
    Identifier.codec.unit(identifier) ~> sized(codec)
}
