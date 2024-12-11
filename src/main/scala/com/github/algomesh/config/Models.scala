package com.github.algomesh.config

case class DatabaseConfig(url: String, username: String, password: String)

case class HttpConfig(host: String, port: Int)
