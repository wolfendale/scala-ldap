package wolfendale.ber

import org.scalatest.freespec._
import org.scalatest.matchers.must._
import scodec._
import scodec.bits._
import wolfendale.EncodingAssertions

class BitStringTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "bitString" - {

    "some bytes" - mustEncode(
      bitString(codecs.bytes(2)),
      hex"ff ff",
      hex"03 03 00 ff ff".bits
    )

    "some bits with a remainder" - mustEncode(
      bitString(codecs.bits(5)),
      bin"11111",
      bin"00000011 00000010 00000011 11111000"
    )

    "0A3B5F291CD" - mustEncode(
      bitString(codecs.bits(44)),
      hex"0A 3B 5F 29 1C D".bits.drop(4),
      hex"03 07 04 0A 3B 5F 29 1C D0".bits
    )
  }
}
