package wolfendale.ldap.core

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class ObjectIdSpec extends AnyFreeSpec with Matchers with EncodingAssertions {

  "ObjectId" - {

    "0.1.2" - mustEncode(
      ObjectId.codec,
      ObjectId(0,1,2),
      hex"04 05 30 2e 31 2e 32".bits
    )
  }
}
