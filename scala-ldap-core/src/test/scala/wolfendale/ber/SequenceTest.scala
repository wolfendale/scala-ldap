package wolfendale.ber

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class SequenceTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "sequence" - {
    "a sequence with a null entry" - mustEncode(
      sequence(berNull),
      (),
      hex"30 02 05 00".bits
    )
  }

  "sequence of" - {
    "a sequence of null entries" - mustEncode(
      sequenceOf(berNull),
      Vector((), ()),
      hex"30 04 05 00 05 00".bits
    )
  }
}
