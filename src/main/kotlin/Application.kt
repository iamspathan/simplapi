package com.iamspathan

import com.iamspathan.db.DatabaseFactory
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.uri
import io.ktor.server.routing.routing
import org.slf4j.event.Level


fun main(args: Array<String>) {
    io.ktor.server.jetty.jakarta.EngineMain.main(args)
}

fun Application.module() {

    install(CallLogging) {
        level = Level.INFO
        format { call ->
            "HTTP ${call.request.httpMethod.value} - ${call.request.uri}"
        }
        filter { call -> call.request.path().startsWith("/") }
    }

    DatabaseFactory.init()
    install(CORS) {
    allowHost("localhost:8081")
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowHeader(HttpHeaders.Authorization)
    allowHeader("MyCustomHeader")
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
        maxAgeInSeconds = 24 * 60 * 60 // 24 hours
    anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    configureRouting()
}
