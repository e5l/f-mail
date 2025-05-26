package com.fmail.backend.infrastructure.summary

import com.fmail.core.domain.email.entities.Email
import com.fmail.core.domain.email.services.EmailSummaryService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class OllamaEmailSummaryService(
    private val ollamaUrl: String = "http://localhost:11434/api/generate",
    private val model: String = "llama3.2"
) : EmailSummaryService {
    
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    override suspend fun generateSummary(email: Email): List<String> {
        val emailContent = buildString {
            appendLine("From: ${email.from.value}")
            appendLine("To: ${email.to.joinToString { it.value }}")
            if (email.cc.isNotEmpty()) {
                appendLine("CC: ${email.cc.joinToString { it.value }}")
            }
            appendLine("Subject: ${email.subject}")
            appendLine()
            appendLine(email.body)
        }
        
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
        
        val bulletPoints = response.response
            .split("\n")
            .filter { it.startsWith("•") || it.startsWith("-") }
            .map { it.removePrefix("•").removePrefix("-").trim() }
            .filter { it.isNotBlank() }
            .take(10)
        
        return if (bulletPoints.size < 10) {
            bulletPoints + List(10 - bulletPoints.size) { "Additional context point ${it + 1}" }
        } else {
            bulletPoints
        }
    }
}

@Serializable
private data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false
)

@Serializable
private data class OllamaResponse(
    val response: String
)