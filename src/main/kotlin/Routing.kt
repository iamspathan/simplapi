package com.iamspathan

import com.iamspathan.db.DatabaseFactory
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlin.text.get

    fun Application.configureRouting() {

        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
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
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 10

                if (page <= 0 || size <= 0) {
                    call.respondText("Page and size must be positive integers", status = HttpStatusCode.BadRequest)
                    return@get
                }

                val users = DatabaseFactory.getUsersPaged(page, size)
                call.respond(users) // Respond with the list of users
            }

            post("/users") {
                val user = call.receive<User>() // Receive the User object from the request body
                val userId = DatabaseFactory.addUser(user) // Add the user to the database
                call.respondText("User with ID $userId added successfully!")
            }

            put("/users/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                    return@put
                }
                val updatedUser = call.receive<User>()
                val success = DatabaseFactory.updateUser(id, updatedUser)
                if (success) {
                    call.respondText("User with ID $id updated successfully!")
                } else {
                    call.respondText("User with ID $id not found", status = HttpStatusCode.NotFound)
                }
            }

            patch("/users/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                    return@patch
                }
                val partialUser = call.receive<Map<String, String>>() // Receive partial updates as a map
                val success = DatabaseFactory.patchUser(id, partialUser)
                if (success) {
                    call.respondText("User with ID $id patched successfully!")
                } else {
                    call.respondText("User with ID $id not found", status = HttpStatusCode.NotFound)
                }
            }

            delete("/users/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
                    return@delete
                }
                val success = DatabaseFactory.deleteUser(id)
                if (success) {
                    call.respondText("User with ID $id deleted successfully!")
                } else {
                    call.respondText("User with ID $id not found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
