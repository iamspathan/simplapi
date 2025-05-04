package com.iamspathan.db

import com.iamspathan.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement() // Automatically increments
    val name: Column<String> = varchar("name", 255)
    val email: Column<String> = varchar("email", 255)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}


fun DatabaseFactory.addUser(user: User): Int {
    return transaction {
        Users
            .insert {
                it[name] = user.name
                it[email] = user.email
            }
            .resultedValues?.get(0)?.get(Users.id) ?: throw Exception("Failed to insert user")
    }
}


object DatabaseFactory {
    fun init() {
        val dbUrl = "jdbc:sqlite:simplapi.db" // SQLite DB file path
        Database.connect(dbUrl, driver = "org.sqlite.JDBC")
        transaction {
            // Initialize the database schema if it doesn't exist
            SchemaUtils.create(Users)
        }
    }

//    fun addUser(id: Int, name: String, email: String) {
//        transaction {
//            Users.insert {
//                it[Users.id] = id
//                it[Users.name] = name
//                it[Users.email] = email
//            }
//        }
//    }

    fun addUser(user: User): Int {
        return transaction {
            Users
                .insert {
                    it[name] = user.name
                    it[email] = user.email
                }
                .resultedValues?.get(0)?.get(Users.id) ?: throw Exception("Failed to insert user")
        }
    }

    fun getUsers(): List<User> {
        return transaction {
            Users.selectAll().map {
                User(it[Users.id].toString(), it[Users.name], it[Users.email])
            }
        }
    }
}