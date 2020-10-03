val commonSettings = Seq(
  scalaVersion := "2.13.3",
  version := "0.1.0"
)

lazy val scalaLdapCore = (project in file("scala-ldap-core"))
  .settings(
    commonSettings,
    name := "scala-ldap-core"
  )
