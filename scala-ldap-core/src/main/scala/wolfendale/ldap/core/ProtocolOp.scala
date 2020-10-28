package wolfendale.ldap.core

import scodec._
import wolfendale.ber
import wolfendale.ber.{ConstructionType, Identifier, TagClass}

sealed abstract class ProtocolOp

object ProtocolOp {

  val codec: Codec[ProtocolOp] =
    codecs.discriminated[ProtocolOp].by(Identifier.codec)
      .typecase(BindRequest.identifier, ber.sized(BindRequest.codec))
      .typecase(BindResponse.identifier, ber.sized(BindResponse.codec))
      .typecase(DelRequest.identifier, ber.sized(DelRequest.codec))
}

final case class BindRequest(
                            version: Int,
                            name: String,
                            authentication: AuthenticationChoice
                            ) extends ProtocolOp

object BindRequest {

  val identifier: Identifier =
    Identifier(TagClass.Application, ConstructionType.Constructed, 0)

  val codec: Codec[BindRequest] = (
    ber.integer ::
    ldapString ::
    AuthenticationChoice.codec
  ).as[BindRequest]
}

// TODO referral
// TODO SASL response
final case class BindResponse(
                               resultCode: ResultCode,
                               matchedDn: String,
                               diagnosticMessage: String
                             ) extends ProtocolOp
object BindResponse {

  val identifier: Identifier =
    Identifier(TagClass.Application, ConstructionType.Constructed, 1)

  val codec: Codec[BindResponse] = (
    ResultCode.codec ::
    ldapString ::
    ldapString
  ).as[BindResponse]
}

final case class DelRequest(distinguishedName: String) extends ProtocolOp

object DelRequest {

  val identifier: Identifier =
    Identifier(TagClass.Application, ConstructionType.Primitive, 10)

  val codec: Codec[DelRequest] =
    codecs.byteAligned(codecs.utf8).as[DelRequest]
}