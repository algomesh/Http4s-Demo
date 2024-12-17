package com.github.algomesh.user

import cats.effect.Async
import cats.implicits.*
import com.github.algomesh.global.http.AbstractController
import com.github.algomesh.global.logging.Logging
import com.github.algomesh.user.model.CreateUser
import com.github.algomesh.user.service.UserService
import io.circe.Json
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.{HttpRoutes, Response}
import org.jooq.exception.IntegrityConstraintViolationException

class UserController[F[_]: Async](service: UserService[F]) extends AbstractController[F], Logging[F] {

  override def path: String = "/users"

  override protected def routes: HttpRoutes[F] =
    HttpRoutes.of {
      case GET -> Root => service.fetchAllUsers.flatMap(users => Ok(users))
      case request @ POST -> Root =>
        request
          .as[CreateUser]
          .flatMap(service.addNewUser)
          .productR(NoContent())
          .recoverWith(handler)
    }

  private def handler: PartialFunction[Throwable, F[Response[F]]] = {
    case error: IntegrityConstraintViolationException => BadRequest(Json.fromString("users.create.duplicate.email"))
  }
}
