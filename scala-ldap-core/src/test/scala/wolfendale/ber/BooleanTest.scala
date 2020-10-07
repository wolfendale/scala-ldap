package wolfendale.ber

import org.scalatest._
import freespec._
import matchers.must._
import wolfendale.EncodingAssertions
import scodec.bits._

class BooleanTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "boolean" - {

    "true" - {

      mustEncode(boolean, true, bin"00000001 00000001 00000001")

      "must decode from any non-0 value" in {
        val result = boolean.decode(bin"00000001 00000001 11111111")
        result.isSuccessful mustBe true
        result.require.remainder mustBe BitVector.empty
        result.require.value mustBe true
      }
    }

    "false" - mustEncode(boolean, false, bin"00000001 00000001 00000000")
  }
}
