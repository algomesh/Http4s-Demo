package com.github.algomesh.jooq

import cats.effect.{Async, Resource, Sync}
import cats.implicits.*
import org.jooq.impl.DSL
import org.jooq.{DSLContext, Insert, Record, Select}

import java.sql.Connection
import javax.sql.DataSource
import scala.jdk.CollectionConverters.*

class Jooq[F[_]: Async](dataSource: DataSource) {

  def fetchAsync[R <: Record](select: Select[R]): F[List[R]] =
    dsl
      .use(context =>
        Async[F].fromCompletionStage(
          Sync[F].delay(context.fetchAsync(select))
        )
      )
      .map(_.asScala.toList)

  def fetchAsync[R <: Record, Out](select: Select[R])(mapper: R => Out): F[List[Out]] =
    dsl
      .use(context =>
        Async[F].fromCompletionStage(
          Sync[F].delay(context.fetchAsync(select))
        )
      )
      .map(_.asScala.map(mapper).toList)

  def insert[R <: Record](insert: Insert[R]): F[Unit] =
    dsl
      .use(context =>
        Async[F].fromCompletionStage(
          Sync[F]
            .delay(insert.attach(context.configuration()))
            .flatMap(_ => Sync[F].delay(insert.executeAsync()))
        )
      )
      .void

  private def dsl: Resource[F, DSLContext] =
    connection.map(DSL.using(_, Dialect))

  private def connection: Resource[F, Connection] =
    Resource.fromAutoCloseable(
      Sync[F].blocking(dataSource.getConnection)
    )
}
