package wolfendale.ldap.core

import scodec._
import wolfendale.{ber, maybe}

abstract class Control {
  def controlType: ObjectId
  def criticality: Boolean
}

object Control {

  implicit lazy val defaultCodec: Codec[Control] =
    GenericControl.codec.upcast[Control]
}

final case class GenericControl(
                                 controlType: ObjectId,
                                 criticality: Boolean,
                                 controlValue: Option[String]
                               ) extends Control

object GenericControl {

  // TODO tests
  val codec: Codec[GenericControl] =
    ber.sequence((
      ObjectId.codec ::
      codecs.withDefaultValue(maybe(ber.boolean), false) ::
      maybe(ldapString)
    ).as[GenericControl])
}

