package com.iamspathan

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.jetty.jakarta.EngineMain.main(args)
}
val users : List<User> = listOf(
    User("1", "Alice", "alice@example.com"),
    User("2", "Bob", "bob@example.com"),
    User("3", "Charlie", "charlie@example.com"),
    User("4", "Diana", "diana@example.com"),
    User("5", "Eve", "eve@example.com"),
    User("6", "Frank", "frank@example.com"),
    User("7", "Grace", "grace@example.com"),
    User("8", "Hank", "hank@example.com"),
    User("9", "Ivy", "ivy@example.com"),
    User("10", "Jack", "jack@example.com")
)


fun Application.module() {
    configureRouting()
}
