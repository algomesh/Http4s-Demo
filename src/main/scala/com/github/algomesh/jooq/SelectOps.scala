package com.github.algomesh.jooq

import org.jooq.{Record, Select}

extension [R <: Record](select: Select[R]) {
  def fetchAsyncF[F[_]: Jooq]: F[List[R]] = summon[Jooq[F]].fetchAsync(select)
}
