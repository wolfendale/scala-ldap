package wolfendale.ber

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class EnumeratedTests extends AnyFreeSpec with Matchers with EncodingAssertions {

  object X extends Enumeration {
    val A, B = Value
  }

  "enumerated" - {

    val codec = enumerated[X.Value](_.id, X.apply)

    "A" - mustEncode(codec, X.A, hex"0a 01 00".bits)
    "B" - mustEncode(codec, X.B, hex"0a 01 01".bits)
  }
}
