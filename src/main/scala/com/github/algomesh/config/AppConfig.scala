package com.github.algomesh.config

import cats.effect.Sync
import cats.implicits.*
import com.typesafe.config.{Config, ConfigFactory}

case class AppConfig(
  databaseConfig: DatabaseConfig,
  httpConfig: HttpConfig
)

object AppConfig {
  def load[F[_]: Sync]: F[AppConfig] =
    for {
      config <- Sync[F].blocking(ConfigFactory.load())

      databaseConfig <- Sync[F].fromEither(parseDatabaseConfig(config.getConfig("database")))
      httpConfig <- Sync[F].fromEither(parseHttpConfig(config.getConfig("http")))
    } yield AppConfig(databaseConfig, httpConfig)

  private[config] def parseDatabaseConfig(config: Config) =
    for {
      url <- Either.catchNonFatal(config.getString("url"))
      username <- Either.catchNonFatal(config.getString("username"))
      password <- Either.catchNonFatal(config.getString("password"))
    } yield DatabaseConfig(url, username, password)

  private[config] def parseHttpConfig(config: Config) =
    for {
      host <- Either.catchNonFatal(config.getString("host"))
      port <- Either.catchNonFatal(config.getInt("port"))
    } yield HttpConfig(host, port)
}
