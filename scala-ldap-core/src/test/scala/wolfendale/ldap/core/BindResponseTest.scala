package wolfendale.ldap.core

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class BindResponseTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "BindResponse" - mustEncode(
    LdapMessage.codec,
    LdapMessage(
      messageId = 1,
      BindResponse(
        resultCode = ResultCode.Success,
        matchedDn = "",
        diagnosticMessage = ""
      )
    ),
    hex"""30 0c
          02 01 01
          61 07
             0a 01 00
             04 00
             04 00""".bits
  )
}
