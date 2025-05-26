package com.fmail.backend.routes

import com.fmail.backend.services.EmailService
import com.fmail.backend.services.OllamaSummaryService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    emailService: EmailService,
    summaryService: OllamaSummaryService
) {
    routing {
        get("/") {
            call.respondText("F-Mail API")
        }
        
        route("/api") {
            emailRoutes(emailService, summaryService)
            authRoutes()
        }
    }
}