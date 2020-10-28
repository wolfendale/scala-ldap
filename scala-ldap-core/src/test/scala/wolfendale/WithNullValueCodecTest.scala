package wolfendale

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import scodec.bits._
import wolfendale.ber.integer

class WithNullValueCodecTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "withNullValue" - {

    "a nullish value" - mustEncode(
      withNullValue(ber.integer, 1337),
      1337,
      BitVector.empty
    )

    "a non-nullish value" - mustEncode(withNullValue(integer, 1337), 50, hex"02 01 32".bits)
  }
}
