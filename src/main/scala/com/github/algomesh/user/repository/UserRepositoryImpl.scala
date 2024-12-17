package com.github.algomesh.user.repository

import com.github.algomesh.jooq.*
import com.github.algomesh.jooq.generated.Tables.USERS
import com.github.algomesh.jooq.generated.tables.records.UsersRecord
import com.github.algomesh.user.model.User
import org.jooq.Record
import org.jooq.impl.DSL

class UserRepositoryImpl[F[_]: Jooq] extends UserRepository[F] {

  override def fetchAllUsers: F[List[User]] =
    DSL
      .selectFrom(USERS)
      .fetchAsyncF(mapper)

//  override def fetchAllUsers: F[List[User]] =
//    summon[Jooq[F]]
//      .fetchAsync(DSL.selectFrom(USERS))(mapper)

  override def addNewUser(user: User): F[Unit] =
    DSL
      .insertInto(USERS)
      .set(
        new UsersRecord(
          id = user.id,
          email = user.email,
          firstName = user.firstName,
          lastName = user.lastName.orNull,
          dob = user.dob,
          createdDate = user.createdDate,
          isEnabled = user.isEnabled
        )
      ).executeAsyncF

  private def mapper(record: Record) =
    User(
      id = record.get(USERS.ID),
      email = record.get(USERS.EMAIL),
      firstName = record.get(USERS.FIRST_NAME),
      lastName = record.getOpt(USERS.LAST_NAME),
      dob = record.get(USERS.DOB),
      createdDate = record.get(USERS.CREATED_DATE),
      isEnabled = record.getBoolean(USERS.IS_ENABLED)
    )

}
