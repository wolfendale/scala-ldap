package wolfendale

import org.scalatest.freespec._
import org.scalatest.matchers.must._
import scodec.Codec
import scodec.bits.BitVector

trait EncodingAssertions { self: AnyFreeSpec with Matchers =>

  def mustEncode[A](codec: Codec[A], value: A, binary: BitVector): Unit = {
    s"must encode to ${binary.toBin}" in {
      val result = codec.encode(value)
      result.isSuccessful mustBe true
      result.require mustEqual binary
    }

    s"must decode from ${binary.toBin}" in {
      val result = codec.decode(binary)
      result.isSuccessful mustBe true
      result.require.remainder mustBe BitVector.empty
      result.require.value mustBe value
    }
  }
}
