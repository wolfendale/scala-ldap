package wolfendale.ldap.core

import scodec.{Attempt, Codec}

import scala.util.Try

final case class ObjectId(parts: Int*) {
  override val toString: String = parts.mkString(".")
}

object ObjectId {

  val codec: Codec[ObjectId] =
    ldapString.exmap(
      { string => Attempt.fromTry { Try {
          string.split("\\.").map(_.toInt)
        }.map(ObjectId(_: _*)) }
      },
      { oid => Attempt.successful(oid.toString) }
    )
}
