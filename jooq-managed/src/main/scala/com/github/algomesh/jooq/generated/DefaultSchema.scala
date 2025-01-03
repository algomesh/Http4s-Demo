/*
 * This file is generated by jOOQ.
 */
package com.github.algomesh.jooq.generated


import com.github.algomesh.jooq.generated.tables.Users

import java.util.Arrays
import java.util.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


object DefaultSchema {

  /**
   * The reference instance of <code>DEFAULT_SCHEMA</code>
   */
  val DEFAULT_SCHEMA = new DefaultSchema
}

/**
 * This class is generated by jOOQ.
 */
class DefaultSchema extends SchemaImpl("", DefaultCatalog.DEFAULT_CATALOG) {

  /**
   * The table <code>USERS</code>.
   */
  def USERS = Users.USERS

  override def getCatalog: Catalog = DefaultCatalog.DEFAULT_CATALOG

  override def getTables: List[Table[_]] = Arrays.asList[Table[_]](
    Users.USERS
  )
}
