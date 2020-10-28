package wolfendale.ldap.core

import enumeratum.values.{IntEnum, IntEnumEntry}
import scodec.Codec
import wolfendale.ber

sealed abstract class ResultCode(val value: Int, val name: String) extends IntEnumEntry

object ResultCode extends IntEnum[ResultCode] {

  override def values: IndexedSeq[ResultCode] = findValues

  case object Success extends ResultCode(0, "success")
  case object OperationsError extends ResultCode(1, "operationsError")
  case object ProtocolError extends ResultCode(2, "protocolError")
  case object TimeLimitExceeded extends ResultCode(3, "timeLimitExceeded")
  case object SizeLimitExceeded extends ResultCode(4, "sizeLimitExceeded")
  case object CompareFalse extends ResultCode(5, "compareFalse")
  case object CompareTrue extends ResultCode(6, "compareTrue")
  case object AuthMethodNotSupported extends ResultCode(7, "authMethodNotSupported")
  case object StrongerAuthRequired extends ResultCode(8, "strongerAuthRequired")
  case object Referral extends ResultCode(10, "referral")
  case object AdminLimitExceeded extends ResultCode(11, "adminLimitExceeded")
  case object UnavailableCriticalExtension extends ResultCode(12, "unavailableCriticalExtension")
  case object ConfidentialityRequired extends ResultCode(13, "confidentialityRequired")
  case object SaslBindInProgress extends ResultCode(14, "saslBindInProgress")
  case object NoSuchAttribute extends ResultCode(16, "noSuchAttribute")
  case object UndefinedAttributeType extends ResultCode(17, "undefinedAttributeType")
  case object InappropriateMatching extends ResultCode(18, "inappropriateMatching")
  case object ConstraintViolation extends ResultCode(19, "constraintViolation")
  case object AttributeOrValueExists extends ResultCode(20, "attributeOrValueExists")
  case object InvalidAttributeSyntax extends ResultCode(21, "invalidAttributeSyntax")
  case object NoSuchObject extends ResultCode(32, "noSuchObject")
  case object AliasProblem extends ResultCode(33, "aliasProblem")
  case object InvalidDnSyntax extends ResultCode(34, "invalidDNSyntax")
  case object AliasDereferencingProblem extends ResultCode(36, "aliasDereferencingProblem")
  case object InappropriateAuthentication extends ResultCode(48, "inappropriateAuthentication")
  case object InvalidCredentials extends ResultCode(49, "invalidCredentials")
  case object InsufficientAccessRights extends ResultCode(50, "insufficientAccessRights")
  case object Busy extends ResultCode(51, "busy")
  case object Unavailable extends ResultCode(52, "unavailable")
  case object UnwillingToPerform extends ResultCode(53, "unwillingToPerform")
  case object LoopDetect extends ResultCode(54, "loopDetect")
  case object NamingViolation extends ResultCode(64, "namingViolation")
  case object ObjectClassViolation extends ResultCode(65, "objectClassViolation")
  case object NotAllowedOnNonLeaf extends ResultCode(66, "notAllowedOnNonLeaf")
  case object NotAllowedOnRdn extends ResultCode(67, "notAllowedOnRDN")
  case object EntryAlreadyExists extends ResultCode(68, "entryAlreadyExists")
  case object ObjectClassModsProhibited extends ResultCode(69, "objectClassModsProhibited")
  case object AffectsMultipleDsas extends ResultCode(71, "affectsMultipleDSAs")
  case object Other extends ResultCode(80, "other")

  val codec: Codec[ResultCode] =
    ber.enumerated[ResultCode](_.value, ResultCode.withValue)
}
