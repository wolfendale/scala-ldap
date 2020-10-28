package wolfendale.ldap.core

import scodec._
import wolfendale.ber.{ConstructionType, Identifier, TagClass}
import wolfendale.{ber, maybe, withNullValue}

final case class LdapMessage(
                              messageId: Int,
                              protocolOp: ProtocolOp,
                              controls: Vector[Control] = Vector.empty
                            )

object LdapMessage {

  def codec(implicit controlCodec: Codec[Control]): Codec[LdapMessage] = {
    ber.sequence((
      ber.integer ::
      ProtocolOp.codec ::
      withNullValue(
        ber.sequenceOf(controlCodec, Identifier(TagClass.ContextSpecific, ConstructionType.Constructed, 0)),
        Vector.empty
      )
    ).as[LdapMessage])
  }
}
