package com.github.algomesh

import cats.effect.{IO, IOApp, Resource}
import com.github.algomesh.config.{AppConfig, DatabaseConfig, HttpConfig}
import com.github.algomesh.global.http.AbstractController
import com.github.algomesh.health.HealthCheckController
import com.github.algomesh.jooq.*
import com.github.algomesh.user.UserController
import com.github.algomesh.user.repository.UserRepositoryImpl
import com.github.algomesh.user.service.UserService
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}

import javax.sql.DataSource
import scala.util.chaining.*

object App extends IOApp.Simple {

  override def run: IO[Unit] =
    (for {
      config <- AppConfig.load[IO].toResource

      dataSource <- datasourceResource(config.databaseConfig)
      given Jooq[IO] = Jooq[IO](dataSource)

      healthCheckController = HealthCheckController[IO]()

      userRepository = UserRepositoryImpl[IO]()
      userService = UserService[IO](userRepository)
      userController = UserController[IO](userService)

      app = httpApp(
        healthCheckController,
        userController
      )

      httpServer <- server(config.httpConfig, app)
    } yield httpServer)
      .use(_ => IO.never)

  private def httpApp(controllers: AbstractController[IO]*): HttpApp[IO] =
    Router(controllers.map(c => (c.path, c.httpRoutes))*).orNotFound

  private def server(config: HttpConfig, app: HttpApp[IO]): Resource[IO, Server] =
    BlazeServerBuilder[IO]
      .bindHttp(config.port, config.host)
      .withHttpApp(app)
      .resource

  private def datasourceResource(databaseConfig: DatabaseConfig): Resource[IO, DataSource] =
    Resource
      .fromAutoCloseable(
        IO.blocking(
          new HikariDataSource()
            .tap(_.setJdbcUrl(databaseConfig.url))
            .tap(_.setUsername(databaseConfig.username))
            .tap(_.setPassword(databaseConfig.password))
        )
      )
      .evalTap(flywayMigration)

  private def flywayMigration(dataSource: DataSource): IO[Unit] =
    IO.blocking {
      Flyway
        .configure()
        .dataSource(dataSource)
        .locations("migration")
        .baselineOnMigrate(true)
        .load()
        .migrate()
    }.void
}
