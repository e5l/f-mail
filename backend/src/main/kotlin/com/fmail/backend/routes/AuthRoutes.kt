package com.fmail.backend.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/auth") {
        get("/google") {
            // TODO: Implement Google OAuth2 flow
            call.respond(HttpStatusCode.NotImplemented, "OAuth2 flow not yet implemented")
        }
        
        get("/callback") {
            // TODO: Handle OAuth2 callback
            call.respond(HttpStatusCode.NotImplemented, "OAuth2 callback not yet implemented")
        }
    }
}