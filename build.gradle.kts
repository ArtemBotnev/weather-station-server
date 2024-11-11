val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val h2_version: String by project
val koin_version = "4.0.0"
val caffeine_version = "3.1.8"

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

group = "com.artembotnev"
version = "0.0.1"

application {
    mainClass.set("com.artembotnev.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
//    implementation("io.ktor:ktor-server-auth-jvm")
//    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("org.postgresql:postgresql:$postgres_version")
//    implementation("com.h2database:h2:$h2_version")
//    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
//    In memory cache
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeine_version")
//    DI
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koin_version"))
    implementation("io.insert-koin:koin-core")
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor")
    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j")
}
