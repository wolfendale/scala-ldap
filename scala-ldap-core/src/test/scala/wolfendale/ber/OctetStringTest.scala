package wolfendale.ber

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import scodec.bits._
import scodec.codecs
import wolfendale.EncodingAssertions

class OctetStringTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "octetString" - {

    "some bytes" - mustEncode(
      octetString(codecs.bytes(2)),
      hex"ff ff",
      hex"04 02 ff ff".bits
    )

    "must pad an inner encoding if it does not produce an output that is byte aligned" - {
      "1111" - mustEncode(
        octetString(codecs.bits(4)),
        bin"1111",
        hex"04 01 f0".bits
      )
    }
  }
}
