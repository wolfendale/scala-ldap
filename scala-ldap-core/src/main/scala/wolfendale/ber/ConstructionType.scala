package wolfendale.ber

import scodec.Codec

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
