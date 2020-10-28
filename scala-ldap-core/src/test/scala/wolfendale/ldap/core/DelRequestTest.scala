package wolfendale.ldap.core

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class DelRequestTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "DelRequest" - {

    "a DelRequest" - mustEncode(LdapMessage.codec,
      LdapMessage(
        messageId = 5,
        protocolOp = DelRequest("dc=example,dc=com"),
        controls = Vector(
          GenericControl(ObjectId(1,2,840,113556,1,4,805), criticality = true, None)
        )
      ),
      hex"""30 35 02 01 05 4a 11 64 63 3d 65 78 61 6d 70 6c
            65 2c 64 63 3d 63 6f 6d a0 1d 30 1b 04 16 31 2e
            32 2e 38 34 30 2e 31 31 33 35 35 36 2e 31 2e 34
            2e 38 30 35 01 01 01""".bits
    )
  }
}