package wolfendale.ldap.core

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class BindRequestTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "BindRequest" - {

    "anonymous simple bind" - mustEncode(
      LdapMessage.codec,
      LdapMessage(
        messageId = 1,
        protocolOp = BindRequest(
          version = 3,
          name = "",
          authentication = AuthenticationChoice.Simple("")
        ),
        controls = Vector.empty
      ),
      hex"""30 0c
               02 01 01
               60 07
                  02 01 03
                  04 00
                  80 00""".bits
    )

    "simple bind" - mustEncode(
      LdapMessage.codec,
      LdapMessage(
        messageId = 1,
        protocolOp = BindRequest(
          version = 3,
          name = "uid=jdoe,ou=People,dc=example,dc=com",
          authentication = AuthenticationChoice.Simple("secret123")
        ),
        controls = Vector.empty
      ),
      hex"""30 39
               02 01 01
               60 34
               02 01 03
               04 24 75 69 64 3d 6a 64 6f 65
                     2c 6f 75 3d 50 65 6f 70
                     6c 65 2c 64 63 3d 65 78
                     61 6d 70 6c 65 2c 64 63
                     3d 63 6f 6d
               80 09 73 65 63 72 65 74 31 32
                     33""".bits
    )
  }
}
