package com.iamspathan

import com.iamspathan.db.DatabaseFactory
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
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
            val users = DatabaseFactory.getUsers()
            call.respond(users)
        }

        post("/users") {
            val user = call.receive<User>() // Receive the User object from the request body
            print(user)
            val userId = DatabaseFactory.addUser(user) // Add the user to the database
            call.respondText("User with ID $userId added successfully!")
        }
    }
}
