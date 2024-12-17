package com.github.algomesh.user.model

import cats.effect.Sync
import cats.effect.std.UUIDGen
import cats.implicits.*
import io.circe.Decoder
import io.scalaland.chimney.dsl.*

import java.time.LocalDate

case class CreateUser(
  email: String,
  firstName: String,
  lastName: Option[String],
  dob: LocalDate
) derives Decoder

object CreateUser {
  extension (obj: CreateUser) {
    def toUserF[F[_]: Sync]: F[User] =
      for {
        uuid <- UUIDGen.randomUUID[F]
        date <- Sync[F].delay(LocalDate.now())
      } yield obj
        .into[User]
        .withFieldConst(_.id, uuid)
        .withFieldConst(_.createdDate, date)
        .withFieldConst(_.isEnabled, true)
        .transform

//    def toUser[F[_]: Sync]: F[User] =
//      UUIDGen
//        .randomUUID[F]
//        .flatMap(uuid =>
//          Sync[F]
//            .delay(LocalDate.now())
//            .map(date =>
//              obj
//                .into[User]
//                .withFieldConst(_.id, uuid)
//                .withFieldConst(_.createdDate, date)
//                .withFieldConst(_.isEnabled, true)
//                .transform
//            )
//        )
  }
}
