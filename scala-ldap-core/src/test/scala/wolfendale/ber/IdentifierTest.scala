package wolfendale.ber

import org.scalatest._
import freespec._
import matchers.must._
import scodec.bits._
import wolfendale.EncodingAssertions

class IdentifierTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "ConstructionType" - {
    "Primitive" - mustEncode(ConstructionType.codec, ConstructionType.Primitive, BitVector.low(1))
    "Constructed" - mustEncode(ConstructionType.codec, ConstructionType.Constructed, BitVector.high(1))
  }

  "Tag Class" - {
    "Universal" - mustEncode(TagClass.codec, TagClass.Universal, bin"00")
    "Application" - mustEncode(TagClass.codec, TagClass.Application, bin"01")
    "ContextSpecific" - mustEncode(TagClass.codec, TagClass.ContextSpecific, bin"10")
    "Private" - mustEncode(TagClass.codec, TagClass.Private, bin"11")
  }

  "Universal TagType" - {

    def mustEncodeTagType(tagType: TagType, binary: BitVector): Unit =
      mustEncode(TagType.universalTagTypeCodec, tagType, binary)

    "EOC" - mustEncodeTagType(TagType.EOC, bin"000000")
    "Boolean" - mustEncodeTagType(TagType.Boolean, bin"000001")
    "Integer" - mustEncodeTagType(TagType.Integer, bin"000010")

    "BitString" -{
      mustEncodeTagType(TagType.BitString(ConstructionType.Primitive), bin"000011")
      mustEncodeTagType(TagType.BitString(ConstructionType.Constructed), bin"100011")
    }

    "OctetString" - {
      mustEncodeTagType(TagType.OctetString(ConstructionType.Primitive), bin"000100")
      mustEncodeTagType(TagType.OctetString(ConstructionType.Constructed), bin"100100")
    }

    "Null" - mustEncodeTagType(TagType.Null, bin"000101")
    "ObjectId" - mustEncodeTagType(TagType.ObjectId, bin"000110")

    "ObjectDescriptor" - {
      mustEncodeTagType(TagType.ObjectDescriptor(ConstructionType.Primitive), bin"000111")
      mustEncodeTagType(TagType.ObjectDescriptor(ConstructionType.Constructed), bin"100111")
    }

    "External" - mustEncodeTagType(TagType.External, bin"101000")
    "Real" - mustEncodeTagType(TagType.Real, bin"001001")
    "Enumerated" - mustEncodeTagType(TagType.Enumerated, bin"001010")
    "EmbeddedPdv" - mustEncodeTagType(TagType.EmbeddedPdv, bin"101011")

    "Utf8String" - {
      mustEncodeTagType(TagType.Utf8String(ConstructionType.Primitive), bin"001100")
      mustEncodeTagType(TagType.Utf8String(ConstructionType.Constructed), bin"101100")
    }

    "RelativeOid" - mustEncodeTagType(TagType.RelativeOid, bin"001101")
    "Time" - mustEncodeTagType(TagType.Time, bin"001110")

    "Reserved" - {

      "must fail to decode reserved value" in {
        TagType.universalTagTypeCodec.decode(bin"001111").isFailure mustBe true
        TagType.universalTagTypeCodec.decode(bin"101111").isFailure mustBe true
      }
    }

    "Sequence" - mustEncodeTagType(TagType.Sequence, bin"110000")
    "Set" - mustEncodeTagType(TagType.Set, bin"110001")

    "NumericString" - {
      mustEncodeTagType(TagType.NumericString(ConstructionType.Primitive), bin"010010")
      mustEncodeTagType(TagType.NumericString(ConstructionType.Constructed), bin"110010")
    }

    "PrintableString" - {
      mustEncodeTagType(TagType.PrintableString(ConstructionType.Primitive), bin"010011")
      mustEncodeTagType(TagType.PrintableString(ConstructionType.Constructed), bin"110011")
    }

    "T61String" - {
      mustEncodeTagType(TagType.T61String(ConstructionType.Primitive), bin"010100")
      mustEncodeTagType(TagType.T61String(ConstructionType.Constructed), bin"110100")
    }

    "VideotexString" - {
      mustEncodeTagType(TagType.VideotexString(ConstructionType.Primitive), bin"010101")
      mustEncodeTagType(TagType.VideotexString(ConstructionType.Constructed), bin"110101")
    }

    "IA5String" - {
      mustEncodeTagType(TagType.IA5String(ConstructionType.Primitive), bin"010110")
      mustEncodeTagType(TagType.IA5String(ConstructionType.Constructed), bin"110110")
    }

    "UTCTime" - {
      mustEncodeTagType(TagType.UtcTime(ConstructionType.Primitive), bin"010111")
      mustEncodeTagType(TagType.UtcTime(ConstructionType.Constructed), bin"110111")
    }

    "GeneralizedTime" - {
      mustEncodeTagType(TagType.GeneralizedTime(ConstructionType.Primitive), bin"011000")
      mustEncodeTagType(TagType.GeneralizedTime(ConstructionType.Constructed), bin"111000")
    }

    "GraphicString" - {
      mustEncodeTagType(TagType.GraphicString(ConstructionType.Primitive), bin"011001")
      mustEncodeTagType(TagType.GraphicString(ConstructionType.Constructed), bin"111001")
    }

    "VisibleString" - {
      mustEncodeTagType(TagType.VisibleString(ConstructionType.Primitive), bin"011010")
      mustEncodeTagType(TagType.VisibleString(ConstructionType.Constructed), bin"111010")
    }

    "GeneralString" - {
      mustEncodeTagType(TagType.GeneralString(ConstructionType.Primitive), bin"011011")
      mustEncodeTagType(TagType.GeneralString(ConstructionType.Constructed), bin"111011")
    }

    "UniversalString" - {
      mustEncodeTagType(TagType.UniversalString(ConstructionType.Primitive), bin"011100")
      mustEncodeTagType(TagType.UniversalString(ConstructionType.Constructed), bin"111100")
    }

    "CharacterString" -{
      mustEncodeTagType(TagType.CharacterString, bin"111101")
    }

    "BmpString" - {
      mustEncodeTagType(TagType.BmpString(ConstructionType.Primitive), bin"011110")
      mustEncodeTagType(TagType.BmpString(ConstructionType.Constructed), bin"111110")
    }

    "Date" - mustEncodeTagType(TagType.Date, bin"011111 00011111")
    "TimeOfDay" - mustEncodeTagType(TagType.TimeOfDay, bin"011111 00100000")
    "DateTime" - mustEncodeTagType(TagType.DateTime, bin"011111 00100001")
    "Duration" - mustEncodeTagType(TagType.Duration, bin"011111 00100010")
    "OidIri" - mustEncodeTagType(TagType.OidIri, bin"011111 00100011")
    "RelativeOidIri" - mustEncodeTagType(TagType.RelativeOidIri, bin"011111 00100100")
  }

  "Other Tag Type" - {
    mustEncode(TagType.otherTagType, TagType.Other(ConstructionType.Primitive, 0), bin"000000")
    mustEncode(TagType.otherTagType, TagType.Other(ConstructionType.Constructed, 0), bin"100000")
    mustEncode(TagType.otherTagType, TagType.Other(ConstructionType.Primitive, 30), bin"011110")
    mustEncode(TagType.otherTagType, TagType.Other(ConstructionType.Primitive, 31), bin"011111 00011111")
  }

  "Identifier" - {

    "A universal identifier" - {
      mustEncode(Identifier.codec, Identifier(TagClass.Universal, TagType.EOC), bin"00000000")
    }

    "A non-universal identifier" - {
      mustEncode(Identifier.codec, Identifier(TagClass.Application, TagType.Other(ConstructionType.Primitive, 0)), bin"01000000")
    }
  }
}
