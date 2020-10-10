package wolfendale.ber

import org.scalatest._
import freespec._
import matchers.must._
import scodec.bits.BitVector
import wolfendale.EncodingAssertions

class ConstructionTypeTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "ConstructionType" - {

    "Primitive" - mustEncode(ConstructionType.codec, ConstructionType.Primitive, BitVector.low(1))
    "Constructed" - mustEncode(ConstructionType.codec, ConstructionType.Constructed, BitVector.high(1))
  }
}
