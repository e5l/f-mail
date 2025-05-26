plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    application
}

application {
    mainClass.set("com.fmail.backend.ApplicationKt")
}

dependencies {
    implementation(project(":core"))
    
    // Ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.serialization.kotlinx.json)
    
    // Ktor Client for Ollama API
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    
    // Email
    implementation(libs.javax.mail)
    implementation(libs.google.api.client)
    implementation(libs.google.oauth.client.jetty)
    implementation(libs.google.api.services.gmail)
    
    // Logging
    implementation(libs.logback.classic)
    
    // Config
    implementation(libs.config4k)
    
    // Testing
    testImplementation(libs.ktor.server.tests)
    testImplementation(kotlin("test"))
}