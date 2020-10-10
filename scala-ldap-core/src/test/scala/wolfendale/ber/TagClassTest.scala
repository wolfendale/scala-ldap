package wolfendale.ber

import org.scalatest._
import freespec._
import matchers.must._
import wolfendale.EncodingAssertions
import scodec.bits._

class TagClassTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "TagClass" - {

    "Universal" - mustEncode(TagClass.codec, TagClass.Universal, bin"00")
    "Application" - mustEncode(TagClass.codec, TagClass.Application, bin"01")
    "ContextSpecific" - mustEncode(TagClass.codec, TagClass.ContextSpecific, bin"10")
    "Private" - mustEncode(TagClass.codec, TagClass.Private, bin"11")
  }
}
