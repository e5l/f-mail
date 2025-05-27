package com.fmail.backend.routes

import com.fmail.backend.services.EmailService
import com.fmail.backend.services.OllamaSummaryService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import java.io.File

fun Application.configureRouting(
    emailService: EmailService,
    summaryService: OllamaSummaryService
) {
    routing {
        // Serve static files from the frontend build output
        staticFiles("/", File("frontend/build/kotlin-webpack/wasmJs/productionExecutable")) {
            default("index.html")
        }
        
        route("/api") {
            emailRoutes(emailService, summaryService)
            authRoutes()
        }
    }
}