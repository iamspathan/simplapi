package com.iamspathan

import com.iamspathan.db.DatabaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.jetty.jakarta.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
}
