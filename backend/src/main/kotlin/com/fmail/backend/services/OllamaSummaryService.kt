package com.fmail.backend.services

import com.fmail.core.api.SummaryService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class OllamaSummaryService : SummaryService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    private val ollamaUrl = "http://localhost:11434/api/generate"
    private val model = "llama3.2"
    
    override suspend fun generateSummary(emailContent: String): List<String> {
        val prompt = """
            Summarize the following email content in exactly 10 bullet points.
            Each bullet point should be concise and capture a key aspect of the email.
            Format each bullet point on a new line starting with "• ".
            
            Email content:
            $emailContent
        """.trimIndent()
        
        val request = OllamaRequest(
            model = model,
            prompt = prompt,
            stream = false
        )
        
        val response: OllamaResponse = client.post(ollamaUrl) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        
        return response.response
            .split("\n")
            .filter { it.startsWith("•") || it.startsWith("-") }
            .map { it.removePrefix("•").removePrefix("-").trim() }
            .take(10)
    }
}

@Serializable
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false
)

@Serializable
data class OllamaResponse(
    val response: String
)