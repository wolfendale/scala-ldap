package wolfendale.ber

import org.scalatest.freespec._
import org.scalatest.matchers.must._
import wolfendale.EncodingAssertions
import scodec.bits._

class IntegerTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "integer" - {
    "0" - mustEncode(integer, 0, hex"02 01 00".bits)
    "50" - mustEncode(integer, 50, hex"02 01 32".bits)
    "50,000" - mustEncode(integer, 50000, hex"02 03 00 c3 50".bits)
    "-12,345" - mustEncode(integer, -12345, hex"02 02 cf c7".bits)

    "must fail to decode if a number is larger than 32 bits" in {
      val result = integer.decode(hex"02 05 ff ff ff ff ff".bits)
      result.isFailure mustBe true
    }
  }
}
