package wolfendale.ldap

import scodec._
import wolfendale.ber

package object core {

  private[core] val ldapString: Codec[String] =
    ber.octetString(codecs.utf8)
}
