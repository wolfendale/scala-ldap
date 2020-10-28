package wolfendale.ber

import scodec.{Attempt, Codec, Err}
import scodec.codecs._
import scodec.bits._

final case class Identifier(tagClass: TagClass, constructionType: ConstructionType, tagNumber: Int)

object Identifier {

  // TODO rewrite as discriminator codec
  private val tagNumberCodec: Codec[Int] = {
    val longTag: Codec[Int] = (constant(bin"11111") ~ BerTagLengthCodec).exmap(
      { case (_, int)        => Attempt.successful(int) },
      { case int if int > 30 => Attempt.successful(() -> int)
        case _               => Attempt.failure(Err("Invalid tag type length"))
      }
    )
    choice(longTag, uint(5))
  }

  val codec: Codec[Identifier] = (
    TagClass.codec ::
    ConstructionType.codec ::
    tagNumberCodec
  ).as[Identifier]
}
