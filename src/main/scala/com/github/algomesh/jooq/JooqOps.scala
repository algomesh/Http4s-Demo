package com.github.algomesh.jooq

import org.jooq.{Insert, Record, Select}

extension [R <: Record](select: Select[R]) {
  def fetchAsyncF[F[_]: Jooq]: F[List[R]] = summon[Jooq[F]].fetchAsync(select)

  def fetchAsyncF[F[_]: Jooq, Out](mapper: R => Out): F[List[Out]] = summon[Jooq[F]].fetchAsync(select)(mapper)
}

extension [R <: Record](insert: Insert[R]) {
  def executeAsyncF[F[_]: Jooq]: F[Unit] = summon[Jooq[F]].insert(insert)
}
