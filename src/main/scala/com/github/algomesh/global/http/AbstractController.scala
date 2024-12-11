package com.github.algomesh.global.http

import cats.effect.Async
import com.github.algomesh.global.logging.Logging
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.{Logger, RequestId}

import scala.util.chaining.*

trait AbstractController[F[_]: Async] extends Http4sDsl[F] { self: Logging[F] =>
  def path: String
  protected def routes: HttpRoutes[F]

  final def httpRoutes: HttpRoutes[F] =
    routes
      .pipe(
        Logger.httpRoutes(
          logHeaders = false,
          logBody = false,
          logAction = Some(info(_))
        )
      )
      .pipe(RequestId.httpRoutes[F])
}
