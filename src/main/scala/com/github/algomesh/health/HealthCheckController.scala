package com.github.algomesh.health

import cats.effect.Async
import com.github.algomesh.global.http.AbstractController
import com.github.algomesh.global.logging.Logging
import com.github.algomesh.health.HealthCheckController.Response
import io.circe.Encoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*

class HealthCheckController[F[_]: Async] extends AbstractController[F], Logging[F] {
  def path: String = "/health"

  protected def routes: HttpRoutes[F] = HttpRoutes.of[F] { case req @ GET -> Root => Ok(Response("Ok")) }
}

object HealthCheckController {
  case class Response(status: String) derives Encoder.AsObject

//  given Encoder[Response] = deriveEncoder
//  implicit lazy val responseEncoder: Encoder[Response] = deriveEncoder
}
