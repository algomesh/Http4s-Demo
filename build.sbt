ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.algomesh"

lazy val scala2Version = "2.13.15"
lazy val configVersion = "1.4.3"
lazy val catsEffectVersion = "3.5.7"
lazy val circeVersion = "0.14.10"
lazy val http4sVersion = "0.23.30"
lazy val logbackClassicVersion = "1.5.7"
lazy val log4catsVersion = "2.7.0"
lazy val http4sBlazeServerVersion = "0.23.17"
lazy val h2Version = "2.3.232"
lazy val hikariCPVersion = "6.2.1"
lazy val flywayCoreVersion = "11.0.1"
lazy val chimneyVersion = "1.5.0"

lazy val root = (project in file("."))
  .settings(
    name := "demo-http4s",
    scalaVersion := "3.5.2"
  )
  .settings(
    libraryDependencies ++=
      Seq(
        // config
        "com.typesafe" % "config" % configVersion,

        // cats
        "org.typelevel" %% "cats-effect" % catsEffectVersion,

        // circe
        "io.circe" %% "circe-core" % circeVersion,
        "io.circe" %% "circe-generic" % circeVersion,
        "io.circe" %% "circe-parser" % circeVersion,

        // http4s
        "org.http4s" %% "http4s-dsl" % http4sVersion,
        "org.http4s" %% "http4s-circe" % http4sVersion,
        "org.http4s" %% "http4s-blaze-server" % http4sBlazeServerVersion,

        // chimney
        "io.scalaland" %% "chimney" % chimneyVersion,

        // logging
        "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
        "org.typelevel" %% "log4cats-slf4j" % log4catsVersion,

        // database
        "com.h2database" % "h2" % h2Version,
        "com.zaxxer" % "HikariCP" % hikariCPVersion,
        "org.flywaydb" % "flyway-core" % flywayCoreVersion
      )
  )
  .settings(
    scalacOptions ++=
      Seq(
        "-no-indent"
      )
  )
  .aggregate(jooqManaged)
  .dependsOn(jooqManaged)

lazy val jooqManaged =
  (project in file("./jooq-managed"))
    .settings(
      name := "jooq-managed",
      scalaVersion := scala2Version,
      jooqVersion := "3.19.6",
      jooqCodegenMode := CodegenMode.Unmanaged,
      autoJooqLibrary := false,
      jooqCodegenConfig := sourceDirectory.value / "main/resources/jooq-codegen.xml",
      libraryDependencies ++=
        Seq(
          // jooq
          "org.jooq" %% "jooq-scala" % jooqVersion.value,

          // jooq codegen
          "com.h2database" % "h2" % h2Version % JooqCodegen,
          "org.jooq" % "jooq-codegen" % jooqVersion.value % JooqCodegen,
          "org.jooq" % "jooq-meta-extensions" % jooqVersion.value % JooqCodegen
        )
    )
    .enablePlugins(JooqCodegenPlugin)
