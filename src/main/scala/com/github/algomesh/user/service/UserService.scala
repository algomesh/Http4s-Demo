package com.github.algomesh.user.service

import cats.effect.Sync
import cats.implicits.*
import com.github.algomesh.user.model.{CreateUser, User}
import com.github.algomesh.user.repository.UserRepository

class UserService[F[_]: Sync](repo: UserRepository[F]) {

  def fetchAllUsers: F[List[User]] = repo.fetchAllUsers
  def addNewUser(createUser: CreateUser): F[Unit] =
    createUser
      .toUserF
      .flatMap(repo.addNewUser)
}
