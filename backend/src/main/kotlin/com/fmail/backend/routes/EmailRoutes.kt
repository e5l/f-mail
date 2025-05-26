package com.fmail.backend.routes

import com.fmail.backend.services.EmailService
import com.fmail.backend.services.OllamaSummaryService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.emailRoutes(
    emailService: EmailService,
    summaryService: OllamaSummaryService
) {
    route("/emails") {
        get {
            try {
                val emails = emailService.fetchEmails()
                call.respond(emails)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }
        
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val email = emailService.getEmail(id)
            if (email != null) {
                call.respond(email)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        
        post("/{id}/read") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            emailService.markAsRead(id)
            call.respond(HttpStatusCode.OK)
        }
        
        post("/{id}/unread") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            emailService.markAsUnread(id)
            call.respond(HttpStatusCode.OK)
        }
        
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            emailService.deleteEmail(id)
            call.respond(HttpStatusCode.OK)
        }
        
        post("/{id}/archive") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            emailService.archiveEmail(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}