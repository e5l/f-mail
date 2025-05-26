package com.fmail.frontend.api

import com.fmail.core.models.Email
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class EmailApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    private val baseUrl = "http://localhost:8080/api"
    
    suspend fun fetchEmails(): List<Email> {
        return client.get("$baseUrl/emails").body()
    }
    
    suspend fun getEmail(id: String): Email {
        return client.get("$baseUrl/emails/$id").body()
    }
    
    suspend fun markAsRead(id: String) {
        client.post("$baseUrl/emails/$id/read")
    }
    
    suspend fun markAsUnread(id: String) {
        client.post("$baseUrl/emails/$id/unread")
    }
    
    suspend fun deleteEmail(id: String) {
        client.delete("$baseUrl/emails/$id")
    }
    
    suspend fun archiveEmail(id: String) {
        client.post("$baseUrl/emails/$id/archive")
    }
}