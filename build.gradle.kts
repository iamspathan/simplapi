val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.2"
    kotlin("plugin.serialization") version "2.1.10"
}

group = "com.iamspathan"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.jetty.jakarta.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-jetty-jakarta")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-cors:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")

    // DB


    implementation("org.jetbrains.exposed:exposed-core:0.39.1") // ORM for database
    implementation("org.jetbrains.exposed:exposed-dao:0.39.1")  // DAO for easy database interactions
    implementation("org.jetbrains.exposed:exposed-jdbc:0.39.1") // JDBC support
    implementation("org.xerial:sqlite-jdbc:3.41.2.2") // SQLite JDBC


    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
