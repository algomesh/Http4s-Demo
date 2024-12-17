package com.github.algomesh.user.repository

import com.github.algomesh.user.model.User

trait UserRepository[F[_]] {
  def fetchAllUsers: F[List[User]]
  def addNewUser(user: User): F[Unit]
}
