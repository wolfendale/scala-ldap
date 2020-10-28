package wolfendale.ldap.core

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class AuthenticationChoiceTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "AuthenticationChoice" - {

    "Simple" - mustEncode(
      AuthenticationChoice.codec,
      AuthenticationChoice.Simple("secret123"),
      hex"80 09 73 65 63 72 65 74 31 32 33".bits
    )
  }
}
