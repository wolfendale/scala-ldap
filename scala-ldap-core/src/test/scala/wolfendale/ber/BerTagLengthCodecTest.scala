package wolfendale.ber

import org.scalatest._
import freespec._
import matchers.must._
import scodec.bits._
import wolfendale.EncodingAssertions

class BerTagLengthCodecTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "BerTagLengthCodec" - {

    "0" - mustEncode(BerTagLengthCodec, 0, bin"00000000")
    "1" - mustEncode(BerTagLengthCodec, 1, bin"00000001")
    "127" - mustEncode(BerTagLengthCodec, 127, bin"01111111")
    "128" - mustEncode(BerTagLengthCodec, 128, bin"10000001 00000000")
    "16383" - mustEncode(BerTagLengthCodec, 16383, bin"11111111 01111111")
    "16384" - mustEncode(BerTagLengthCodec, 16384, bin"10000001 10000000 00000000")
    "Int.MaxValue" - mustEncode(BerTagLengthCodec, Int.MaxValue, bin"10000111 11111111 11111111 11111111 01111111")

    "Binary strings which would decode to a value greater than Int.MaxValue must fail instead" ignore {
      // TODO
    }
  }
}
