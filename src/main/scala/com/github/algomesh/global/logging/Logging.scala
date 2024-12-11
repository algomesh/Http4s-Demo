package com.github.algomesh.global.logging

import cats.effect.Sync
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait Logging[F[_]: Sync] {
  lazy val log: Logger[F] = Slf4jLogger.getLoggerFromClass[F](getClass)

  def info(message: => String): F[Unit] = log.info(message)
}
