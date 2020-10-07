package wolfendale.ber

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import scodec.bits.HexStringSyntax
import wolfendale.EncodingAssertions

class SetTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "set" - {

    "a set with a null entry" - mustEncode(
      set(berNull),
      (),
      hex"31 02 05 00".bits
    )
  }

  "set of" - {

    "a set of boolean values" - mustEncode(
      setOf(boolean),
      Set(true, false),
      hex"31 06 01 01 01 01 01 00".bits
    )
  }
}
