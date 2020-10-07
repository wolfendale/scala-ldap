package wolfendale.ber

import org.scalatest._
import freespec._
import matchers.must._
import wolfendale.EncodingAssertions
import scodec._
import bits._

class BerTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "BerTest" - {

    "ber" - {

      "empty content" - {
        mustEncode(
          ber[BitVector](Identifier(TagClass.Universal, TagType.Null), codecs.bits(0)),
          BitVector.empty,
          bin"00000101 00000000"
        )
      }

      "short content" - {
        mustEncode(
          ber[ByteVector](Identifier(TagClass.Universal, TagType.OctetString(ConstructionType.Primitive)), codecs.bytes(1)),
          ByteVector.high(1),
          bin"00000100 00000001 11111111"
        )
      }

      "short content (max)" - {
        mustEncode(
          ber[ByteVector](Identifier(TagClass.Universal, TagType.OctetString(ConstructionType.Primitive)), codecs.bytes(127)),
          ByteVector.high(127),
          (bin"00000100 01111111" ++ ByteVector.high(127).bits).compact
        )
      }

      "long content" - {
        mustEncode(
          ber[ByteVector](Identifier(TagClass.Universal, TagType.OctetString(ConstructionType.Primitive)), codecs.bytes(128)),
          ByteVector.high(128),
          (bin"00000100 10000001 10000000" ++ ByteVector.high(128).bits).compact
        )
      }

      "content of indefinite length is not supported" in {
        val codec = ber(Identifier(TagClass.Universal, TagType.OctetString(ConstructionType.Primitive)), codecs.bytes(1))
        val result = codec.decode(bin"00000100 10000000 11111111")
        result.isFailure mustBe true
      }

      "content with a reserved length octet should be rejected" in {
        val codec = ber(Identifier(TagClass.Universal, TagType.OctetString(ConstructionType.Primitive)), codecs.bytes(1))
        val result = codec.decode(bin"00000100 11111111 11111111")
        result.isFailure mustBe true
      }

      "content with a content length represented in more than 64 bits is unsupported" in {
        val codec = ber(Identifier(TagClass.Universal, TagType.OctetString(ConstructionType.Primitive)), codecs.bytes(1))
        val result = codec.decode(bin"00000100 10001001 11111111")
        result.isFailure mustBe true
        result.asInstanceOf[Attempt.Failure].cause.message mustEqual "Content lengths that are greater than (2 ^ 64) bytes are not supported"
      }
    }
  }
}
