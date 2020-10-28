package wolfendale.ldap.core

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import wolfendale.EncodingAssertions
import scodec.bits._

class ResultCodeTest extends AnyFreeSpec with Matchers with EncodingAssertions {

  "ResultCode" - {

    "Success" - mustEncode(ResultCode.codec, ResultCode.Success, hex"0a 01 00".bits)
    "OperationsError" - mustEncode(ResultCode.codec, ResultCode.OperationsError, hex"0a 01 01".bits)
    "ProtocolError" - mustEncode(ResultCode.codec, ResultCode.ProtocolError, hex"0a 01 02".bits)
    "TimeLimitExceeded" - mustEncode(ResultCode.codec, ResultCode.TimeLimitExceeded, hex"0a 01 03".bits)
    "SizeLimitExceeded" - mustEncode(ResultCode.codec, ResultCode.SizeLimitExceeded, hex"0a 01 04".bits)
    "CompareFalse" - mustEncode(ResultCode.codec, ResultCode.CompareFalse, hex"0a 01 05".bits)
    "CompareTrue" - mustEncode(ResultCode.codec, ResultCode.CompareTrue, hex"0a 01 06".bits)
    "AuthMethodNotSupported" - mustEncode(ResultCode.codec, ResultCode.AuthMethodNotSupported, hex"0a 01 07".bits)
    "StrongerAuthRequired" - mustEncode(ResultCode.codec, ResultCode.StrongerAuthRequired, hex"0a 01 08".bits)
    "Referral" - mustEncode(ResultCode.codec, ResultCode.Referral, hex"0a 01 0a".bits)
    "AdminLimitExceeded" - mustEncode(ResultCode.codec, ResultCode.AdminLimitExceeded, hex"0a 01 0b".bits)
    "UnavailableCriticalExtension" - mustEncode(ResultCode.codec, ResultCode.UnavailableCriticalExtension, hex"0a 01 0c".bits)
    "ConfidentialityRequired" - mustEncode(ResultCode.codec, ResultCode.ConfidentialityRequired, hex"0a 01 0d".bits)
    "SaslBindInProgress" - mustEncode(ResultCode.codec, ResultCode.SaslBindInProgress, hex"0a 01 0e".bits)
    "NoSuchAttribute" - mustEncode(ResultCode.codec, ResultCode.NoSuchAttribute, hex"0a 01 10".bits)
    "UndefinedAttributeType" - mustEncode(ResultCode.codec, ResultCode.UndefinedAttributeType, hex"0a 01 11".bits)
    "InappropriateMatching" - mustEncode(ResultCode.codec, ResultCode.InappropriateMatching, hex"0a 01 12".bits)
    "ConstraintViolation" - mustEncode(ResultCode.codec, ResultCode.ConstraintViolation, hex"0a 01 13".bits)
    "AttributeOrValueExists" - mustEncode(ResultCode.codec, ResultCode.AttributeOrValueExists, hex"0a 01 14".bits)
    "InvalidAttributeSyntax" - mustEncode(ResultCode.codec, ResultCode.InvalidAttributeSyntax, hex"0a 01 15".bits)
    "NoSuchObject" - mustEncode(ResultCode.codec, ResultCode.NoSuchObject, hex"0a 01 20".bits)
    "AliasProblem" - mustEncode(ResultCode.codec, ResultCode.AliasProblem, hex"0a 01 21".bits)
    "InvalidDnSyntax" - mustEncode(ResultCode.codec, ResultCode.InvalidDnSyntax, hex"0a 01 22".bits)
    "AliasDereferencingProblem" - mustEncode(ResultCode.codec, ResultCode.AliasDereferencingProblem, hex"0a 01 24".bits)
    "InappropriateAuthentication" - mustEncode(ResultCode.codec, ResultCode.InappropriateAuthentication, hex"0a 01 30".bits)
    "InvalidCredentials" - mustEncode(ResultCode.codec, ResultCode.InvalidCredentials, hex"0a 01 31".bits)
    "InsufficientAccessRights" - mustEncode(ResultCode.codec, ResultCode.InsufficientAccessRights, hex"0a 01 32".bits)
    "Busy" - mustEncode(ResultCode.codec, ResultCode.Busy, hex"0a 01 33".bits)
    "Unavailable" - mustEncode(ResultCode.codec, ResultCode.Unavailable, hex"0a 01 34".bits)
    "UnwillingToPerform" - mustEncode(ResultCode.codec, ResultCode.UnwillingToPerform, hex"0a 01 35".bits)
    "LoopDetect" - mustEncode(ResultCode.codec, ResultCode.LoopDetect, hex"0a 01 36".bits)
    "NamingViolation" - mustEncode(ResultCode.codec, ResultCode.NamingViolation, hex"0a 01 40".bits)
    "ObjectClassViolation" - mustEncode(ResultCode.codec, ResultCode.ObjectClassViolation, hex"0a 01 41".bits)
    "NotAllowedOnNonLeaf" - mustEncode(ResultCode.codec, ResultCode.NotAllowedOnNonLeaf, hex"0a 01 42".bits)
    "NotAllowedOnRdn" - mustEncode(ResultCode.codec, ResultCode.NotAllowedOnRdn, hex"0a 01 43".bits)
    "EntryAlreadyExists" - mustEncode(ResultCode.codec, ResultCode.EntryAlreadyExists, hex"0a 01 44".bits)
    "ObjectClassModsProhibited" - mustEncode(ResultCode.codec, ResultCode.ObjectClassModsProhibited, hex"0a 01 45".bits)
    "AffectsMultipleDsas" - mustEncode(ResultCode.codec, ResultCode.AffectsMultipleDsas, hex"0a 01 47".bits)
    "Other" - mustEncode(ResultCode.codec, ResultCode.Other, hex"0a 01 50".bits)
  }
}
