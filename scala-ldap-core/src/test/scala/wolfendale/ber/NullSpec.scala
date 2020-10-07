package wolfendale.ber

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class NullSpec extends AnyFreeSpec with Matchers with EncodingAssertions {

  "berNull" - {

    "()" - mustEncode(berNull, (), hex"05 00".bits)
  }
}
