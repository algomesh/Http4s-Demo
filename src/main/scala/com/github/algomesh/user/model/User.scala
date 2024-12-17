package com.github.algomesh.user.model

import io.circe.Encoder

import java.time.LocalDate
import java.util.UUID

case class User(
  id: UUID,
  email: String,
  firstName: String,
  lastName: Option[String],
  dob: LocalDate,
  createdDate: LocalDate,
  isEnabled: Boolean
) derives Encoder.AsObject
