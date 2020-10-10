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
