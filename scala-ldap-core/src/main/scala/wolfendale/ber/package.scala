package wolfendale

import scodec.{Attempt, Codec, DecodeResult, SizeBound}
import scodec.bits._
import scodec.codecs._

import scala.annotation.tailrec

package object ber {

  def berInt: Codec[Int] = new Codec[Int] {

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
}
