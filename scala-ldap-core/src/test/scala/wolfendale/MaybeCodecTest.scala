package wolfendale

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import scodec.bits._

class MaybeCodecTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "maybe" - {

    "None" - mustEncode(
      maybe(ber.integer),
      None,
      BitVector.empty
    )

    "1" - mustEncode(
      maybe(ber.integer),
      Some(1),
      hex"02 01 01".bits
    )
  }
}
