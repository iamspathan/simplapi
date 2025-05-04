package com.iamspathan

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureRouting() {

    install(ContentNegotiation) {
        json(Json { prettyPrint =true})
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/greet/{name}") {
            val name = call.parameters["name"] ?: "Guest"
            call.respondText("Hello $name!")
        }

        get("/users") {
            call.respond(users)
        }


    }
}
