package wolfendale.ber

import scodec.{Attempt, Codec, DecodeResult, SizeBound}
import scodec.bits.BitVector

import scala.annotation.tailrec
import scala.util.Try

object BerIntegerCodec extends Codec[Int] {

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
