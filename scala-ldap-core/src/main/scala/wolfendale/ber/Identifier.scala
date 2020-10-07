package wolfendale.ber

import scodec.{Attempt, Codec, Err}

sealed abstract class TagClass

object TagClass {

  case object Universal extends TagClass
  case object Application extends TagClass
  case object ContextSpecific extends TagClass
  case object Private extends TagClass

  val codec: Codec[TagClass] = {
    import scodec.codecs._
    uint2.exmap({
      case 0 => Attempt.successful(Universal)
      case 1 => Attempt.successful(Application)
      case 2 => Attempt.successful(ContextSpecific)
      case 3 => Attempt.successful(Private)
      case _ => Attempt.failure(Err("Impossible"))
    }, {
      case Universal       => Attempt.successful(0)
      case Application     => Attempt.successful(1)
      case ContextSpecific => Attempt.successful(2)
      case Private         => Attempt.successful(3)
    })
  }
}

sealed abstract class TagType {
  val constructionType: ConstructionType
}

object TagType {

  sealed abstract class WithConstructionType(val constructionType: ConstructionType) extends TagType

  case object EOC extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object Boolean extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object Integer extends TagType.WithConstructionType(ConstructionType.Primitive)
  final case class BitString(constructionType: ConstructionType) extends TagType
  final case class OctetString(constructionType: ConstructionType) extends TagType
  case object Null extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object ObjectId extends TagType.WithConstructionType(ConstructionType.Primitive)
  final case class ObjectDescriptor(constructionType: ConstructionType) extends TagType
  case object External extends TagType.WithConstructionType(ConstructionType.Constructed)
  case object Real extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object Enumerated extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object EmbeddedPdv extends TagType.WithConstructionType(ConstructionType.Constructed)
  final case class Utf8String(constructionType: ConstructionType) extends TagType
  case object RelativeOid extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object Time extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object Sequence extends TagType.WithConstructionType(ConstructionType.Constructed)
  case object Set extends TagType.WithConstructionType(ConstructionType.Constructed)
  final case class NumericString(constructionType: ConstructionType) extends TagType
  final case class PrintableString(constructionType: ConstructionType) extends TagType
  final case class T61String(constructionType: ConstructionType) extends TagType
  final case class VideotexString(constructionType: ConstructionType) extends TagType
  final case class IA5String(constructionType: ConstructionType) extends TagType
  final case class UtcTime(constructionType: ConstructionType) extends TagType
  final case class GeneralizedTime(constructionType: ConstructionType) extends TagType
  final case class GraphicString(constructionType: ConstructionType) extends TagType
  final case class VisibleString(constructionType: ConstructionType) extends TagType
  final case class GeneralString(constructionType: ConstructionType) extends TagType
  final case class UniversalString(constructionType: ConstructionType) extends TagType
  case object CharacterString extends TagType.WithConstructionType(ConstructionType.Constructed)
  final case class BmpString(constructionType: ConstructionType) extends TagType
  case object Date extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object TimeOfDay extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object DateTime extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object Duration extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object OidIri extends TagType.WithConstructionType(ConstructionType.Primitive)
  case object RelativeOidIri extends TagType.WithConstructionType(ConstructionType.Primitive)
  final case class Other(constructionType: ConstructionType, tagNumber: Int) extends TagType

  private val tagNumber: Codec[Int] = {
    import scodec.bits._
    import scodec.codecs._
    val longTag: Codec[Int] = (constant(bin"11111") ~ berInt).exmap(
      { case (_, int)        => Attempt.successful(int) },
      { case int if int > 30 => Attempt.successful(() -> int)
        case _               => Attempt.failure(Err("Invalid tag type length"))
      }
    )
    choice(longTag, uint(5))
  }

  val otherTagType: Codec[TagType] =
    (ConstructionType.codec ~ tagNumber).exmap(
      Other.tupled.andThen(Attempt.successful),
      { case Other(ct, tn) => Attempt.successful((ct, tn))
        case _             => Attempt.failure(Err("Unable to encode universal tag type with generic codec"))
      }
    )

  val universalTagTypeCodec: Codec[TagType] = {
    (ConstructionType.codec ~ tagNumber).exmap({
      case (ConstructionType.Primitive, 0)    => Attempt.successful(TagType.EOC)
      case (ConstructionType.Primitive, 1)    => Attempt.successful(TagType.Boolean)
      case (ConstructionType.Primitive, 2)    => Attempt.successful(TagType.Integer)
      case (constructionType, 3)              => Attempt.successful(TagType.BitString(constructionType))
      case (constructionType, 4)              => Attempt.successful(TagType.OctetString(constructionType))
      case (ConstructionType.Primitive, 5)    => Attempt.successful(TagType.Null)
      case (ConstructionType.Primitive, 6)    => Attempt.successful(TagType.ObjectId)
      case (constructionType, 7)              => Attempt.successful(TagType.ObjectDescriptor(constructionType))
      case (ConstructionType.Constructed, 8)  => Attempt.successful(TagType.External)
      case (ConstructionType.Primitive, 9)    => Attempt.successful(TagType.Real)
      case (ConstructionType.Primitive, 10)   => Attempt.successful(TagType.Enumerated)
      case (ConstructionType.Constructed, 11) => Attempt.successful(TagType.EmbeddedPdv)
      case (constructionType, 12)             => Attempt.successful(TagType.Utf8String(constructionType))
      case (ConstructionType.Primitive, 13)   => Attempt.successful(TagType.RelativeOid)
      case (ConstructionType.Primitive, 14)   => Attempt.successful(TagType.Time)
      case (ConstructionType.Constructed, 16) => Attempt.successful(TagType.Sequence)
      case (ConstructionType.Constructed, 17) => Attempt.successful(TagType.Set)
      case (constructionType, 18)             => Attempt.successful(TagType.NumericString(constructionType))
      case (constructionType, 19)             => Attempt.successful(TagType.PrintableString(constructionType))
      case (constructionType, 20)             => Attempt.successful(TagType.T61String(constructionType))
      case (constructionType, 21)             => Attempt.successful(TagType.VideotexString(constructionType))
      case (constructionType, 22)             => Attempt.successful(TagType.IA5String(constructionType))
      case (constructionType, 23)             => Attempt.successful(TagType.UtcTime(constructionType))
      case (constructionType, 24)             => Attempt.successful(TagType.GeneralizedTime(constructionType))
      case (constructionType, 25)             => Attempt.successful(TagType.GraphicString(constructionType))
      case (constructionType, 26)             => Attempt.successful(TagType.VisibleString(constructionType))
      case (constructionType, 27)             => Attempt.successful(TagType.GeneralString(constructionType))
      case (constructionType, 28)             => Attempt.successful(TagType.UniversalString(constructionType))
      case (ConstructionType.Constructed, 29) => Attempt.successful(TagType.CharacterString)
      case (constructionType, 30)             => Attempt.successful(TagType.BmpString(constructionType))
      case (ConstructionType.Primitive, 31)   => Attempt.successful(TagType.Date)
      case (ConstructionType.Primitive, 32)   => Attempt.successful(TagType.TimeOfDay)
      case (ConstructionType.Primitive, 33)   => Attempt.successful(TagType.DateTime)
      case (ConstructionType.Primitive, 34)   => Attempt.successful(TagType.Duration)
      case (ConstructionType.Primitive, 35)   => Attempt.successful(TagType.OidIri)
      case (ConstructionType.Primitive, 36)   => Attempt.successful(TagType.RelativeOidIri)
      case _                                  => Attempt.failure(Err("Invalid tag type"))
    }, {
      case TagType.EOC                                => Attempt.successful((ConstructionType.Primitive, 0))
      case TagType.Boolean                            => Attempt.successful((ConstructionType.Primitive, 1))
      case TagType.Integer                            => Attempt.successful((ConstructionType.Primitive, 2))
      case TagType.BitString(constructionType)        => Attempt.successful((constructionType, 3))
      case TagType.OctetString(constructionType)      => Attempt.successful((constructionType, 4))
      case TagType.Null                               => Attempt.successful((ConstructionType.Primitive, 5))
      case TagType.ObjectId                           => Attempt.successful((ConstructionType.Primitive, 6))
      case TagType.ObjectDescriptor(constructionType) => Attempt.successful((constructionType, 7))
      case TagType.External                           => Attempt.successful((ConstructionType.Constructed, 8))
      case TagType.Real                               => Attempt.successful((ConstructionType.Primitive, 9))
      case TagType.Enumerated                         => Attempt.successful((ConstructionType.Primitive, 10))
      case TagType.EmbeddedPdv                        => Attempt.successful((ConstructionType.Constructed, 11))
      case TagType.Utf8String(constructionType)       => Attempt.successful((constructionType, 12))
      case TagType.RelativeOid                        => Attempt.successful((ConstructionType.Primitive, 13))
      case TagType.Time                               => Attempt.successful((ConstructionType.Primitive, 14))
      case TagType.Sequence                           => Attempt.successful((ConstructionType.Constructed, 16))
      case TagType.Set                                => Attempt.successful((ConstructionType.Constructed, 17))
      case TagType.NumericString(constructionType)    => Attempt.successful((constructionType, 18))
      case TagType.PrintableString(constructionType)  => Attempt.successful((constructionType, 19))
      case TagType.T61String(constructionType)        => Attempt.successful((constructionType, 20))
      case TagType.VideotexString(constructionType)   => Attempt.successful((constructionType, 21))
      case TagType.IA5String(constructionType)        => Attempt.successful((constructionType, 22))
      case TagType.UtcTime(constructionType)          => Attempt.successful((constructionType, 23))
      case TagType.GeneralizedTime(constructionType)  => Attempt.successful((constructionType, 24))
      case TagType.GraphicString(constructionType)    => Attempt.successful((constructionType, 25))
      case TagType.VisibleString(constructionType)    => Attempt.successful((constructionType, 26))
      case TagType.GeneralString(constructionType)    => Attempt.successful((constructionType, 27))
      case TagType.UniversalString(constructionType)  => Attempt.successful((constructionType, 28))
      case TagType.CharacterString                    => Attempt.successful((ConstructionType.Constructed, 29))
      case TagType.BmpString(constructionType)        => Attempt.successful((constructionType, 30))
      case TagType.Date                               => Attempt.successful((ConstructionType.Primitive, 31))
      case TagType.TimeOfDay                          => Attempt.successful((ConstructionType.Primitive, 32))
      case TagType.DateTime                           => Attempt.successful((ConstructionType.Primitive, 33))
      case TagType.Duration                           => Attempt.successful((ConstructionType.Primitive, 34))
      case TagType.OidIri                             => Attempt.successful((ConstructionType.Primitive, 35))
      case TagType.RelativeOidIri                     => Attempt.successful((ConstructionType.Primitive, 36))
      case _                                          => Attempt.failure(Err("Invalid Universal Tag"))
    })
  }
}

sealed abstract class ConstructionType

object ConstructionType {

  case object Primitive extends ConstructionType
  case object Constructed extends ConstructionType

  val codec: Codec[ConstructionType] = {
    import scodec.codecs._
    bool.xmap({
      case false => Primitive
      case true  => Constructed
    }, {
      case Primitive   => false
      case Constructed => true
    })
  }
}

final case class Identifier(tagClass: TagClass, tagType: TagType)

object Identifier extends ((TagClass, TagType) => Identifier) {

  val codec: Codec[Identifier] = {
    TagClass.codec.flatZip {
      case TagClass.Universal => TagType.universalTagTypeCodec
      case _                  => TagType.otherTagType
    }.xmap(Identifier.tupled, Function.unlift(Identifier.unapply))
  }
}