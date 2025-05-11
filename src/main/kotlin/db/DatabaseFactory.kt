package com.iamspathan.db

import com.iamspathan.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import kotlin.text.get
import kotlin.text.insert
import kotlin.text.set
import kotlin.toString
import com.iamspathan.gravatar.GravatarUtil

object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement() // Automatically increments
    val name: Column<String> = varchar("name", 255)
    val email: Column<String> = varchar("email", 255)
    val avatarUrl: Column<String> = varchar("avatar_url", 512).default("")
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

fun DatabaseFactory.addUser(user: User): Int {
    return transaction {
        Users
            .insert {
                it[name] = user.name
                it[email] = user.email
                it[avatarUrl] = GravatarUtil.generateGravatarUrl(user.email)
            }
            .resultedValues?.get(0)?.get(Users.id) ?: throw Exception("Failed to insert user")
    }
}


object DatabaseFactory {
    fun init() {
        val dbUrl = "jdbc:sqlite:simplapi.db" // SQLite DB file path
        Database.connect(dbUrl, driver = "org.sqlite.JDBC")
        transaction {

            if (!Users.columns.any { it.name == "avatar_url" }) {
                exec("ALTER TABLE Users ADD COLUMN avatar_url VARCHAR(512) DEFAULT '' NOT NULL")
            }
            SchemaUtils.createMissingTablesAndColumns(Users)

            // Pre-populate the database with static values
            val users: List<User> = listOf(
                User("1", "Alice", "alice@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "alice@example.com")),
                User("2", "Bob", "bob@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "bob@example.com")),
                User("3", "Charlie", "charlie@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "charlie@example.com")),
                User("4", "Diana", "diana@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "diana@example.com")),
                User("5", "Eve", "eve@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "eve@example.com")),
                User("6", "Frank", "frank@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "frank@example.com")),
                User("7", "Grace", "grace@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "grace@example.com")),
                User("8", "Hank", "hank@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "hank@example.com")),
                User("9", "Ivy", "ivy@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "ivy@example.com")),
                User("10", "Jack", "jack@example.com", avatarUrl = GravatarUtil.generateGravatarUrl(email = "jack@example.com"))
            )
            // Insert users into the database if not already present
            users.forEach { user ->
                if (Users.select { Users.id eq user.id.toInt() }.empty()) {
                    Users.insert {
                        it[id] = user.id.toInt()
                        it[name] = user.name
                        it[email] = user.email
                        it[avatarUrl] = user.avatarUrl ?: ""
                    }
                }
            }
        }
    }


    fun getUsersPaged(page: Int, size: Int): List<User> {
        return transaction {
            Users.selectAll()
                .limit(size, offset = ((page - 1) * size).toLong())
                .map {
                    User(it[Users.id].toString(), it[Users.name], it[Users.email], avatarUrl=GravatarUtil.generateGravatarUrl(it[Users.id].toString()))
                }
        }
    }

    fun addUser(user: User): Int {
        return transaction {
            Users
                .insert {
                    it[name] = user.name
                    it[email] = user.email
                    it[avatarUrl] = GravatarUtil.generateGravatarUrl(user.email)
                }
                .resultedValues?.get(0)?.get(Users.id) ?: throw Exception("Failed to insert user")
        }
    }

    fun getUsers(): List<User> {
        return transaction {
            Users.selectAll().map {
                User(it[Users.id].toString(), it[Users.name], it[Users.email], avatarUrl = GravatarUtil.generateGravatarUrl(it[Users.id].toString()))
            }
        }
    }

    fun updateUser(id: Int, user: User): Boolean {
        return transaction {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[email] = user.email
                it[avatarUrl] = GravatarUtil.generateGravatarUrl(user.email)
            } > 0
        }
    }

    fun patchUser(id: Int, updates: Map<String, String>): Boolean {
        return transaction {
            val updateCount = Users.update({ Users.id eq id }) {
                updates["name"]?.let {name -> it[Users.name] = name }
                updates["email"]?.let {email -> it[Users.email] = email
                updates["avatarUrl"]?.let { avatarUrl -> it[Users.avatarUrl] = GravatarUtil.generateGravatarUrl(email) }
                }
            }
            updateCount > 0
        }
    }

    fun deleteUser(id: Int): Boolean {
        return transaction {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }
}