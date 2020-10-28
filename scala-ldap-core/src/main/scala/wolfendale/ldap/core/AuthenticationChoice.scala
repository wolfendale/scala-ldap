package wolfendale.ldap.core

import scodec.{Codec, TransformSyntax, codecs}
import wolfendale.ber
import wolfendale.ber.{ConstructionType, Identifier, TagClass}

sealed abstract class AuthenticationChoice

object AuthenticationChoice {

  final case class Simple(password: String) extends AuthenticationChoice

  object Simple {

    val identifier: Identifier = Identifier(TagClass.ContextSpecific, ConstructionType.Primitive, 0)

    val codec: Codec[Simple] =
      ber.octetString(codecs.utf8, Identifier(TagClass.ContextSpecific, ConstructionType.Primitive, 0)).as[Simple]
  }

  val codec: Codec[AuthenticationChoice] =
    codecs.discriminated[AuthenticationChoice].by(Identifier.codec)
      .typecase(Simple.identifier, ber.sized(codecs.utf8.as[Simple]))
      // TODO Sasl Auth
      // TODO Other auth?
}
