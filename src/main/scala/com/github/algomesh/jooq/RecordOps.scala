package com.github.algomesh.jooq

import org.jooq.{Field, Record}

extension (record: Record) {
  def getOpt[T](field: Field[T]): Option[T] = Option(record.get(field))
  
  def getBoolean(field: Field[JBoolean]): Boolean = record.get(field).booleanValue()
}
