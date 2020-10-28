package wolfendale.ber

import scodec.{Attempt, Codec, DecodeResult, Decoder, Err, SizeBound}
import scodec.bits.BitVector
import scodec.codecs.{bool, fail, fixedSizeBytes, ulong}

import scala.annotation.tailrec

class BerContentSizeCodec[A](codec: Codec[A]) extends Codec[A] {

  private val sizeLimit: Option[Long] =
    codec.sizeBound.upperBound

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
        case 0 =>
          fail[Long](Err("Content of indefinite length is not supported"))
        case 127 =>
          fail[Long](Err("Length value of 0xFF is reserved"))
        case size if sizeLimit.exists(size > _) =>
          fail[Long](Err(s"This field only supports content up to $sizeLimit bytes"))
        case size if size <= 8 =>
          ulong(size.toInt * 8)
        case _ =>
          fail[Long](Err("Content lengths that are greater than (2 ^ 64) bytes are not supported"))
      } (efficientSize)
    } (java.lang.Long.compareUnsigned(_, 127L) > 0)
  }

  private val decoder: Decoder[A] =
    lengthCodec.flatMap(fixedSizeBytes(_, codec))

  override val sizeBound: SizeBound = SizeBound.atLeast(8)

  override def encode(value: A): Attempt[BitVector] = for {
    content <- codec.encode(value)
    size    <- lengthCodec.encode(content.size / 8)
  } yield size ++ content

  override def decode(bits: BitVector): Attempt[DecodeResult[A]] =
    decoder.decode(bits)
}
