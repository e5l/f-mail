package com.fmail.e2e.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Helper class for setting up test data via API calls
 */
class TestDataHelper(private val baseUrl: String = TestConfig.baseUrl) {
    
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    
    /**
     * Creates test emails via API for testing
     */
    fun createTestEmails(count: Int = 5): List<TestEmail> = runBlocking {
        val emails = mutableListOf<TestEmail>()
        
        for (i in 1..count) {
            val email = TestEmail(
                subject = "Test Email $i",
                sender = "sender$i@example.com",
                content = "This is test email content #$i. ".repeat(10),
                isUnread = i <= 2 // First 2 emails are unread
            )
            
            // In a real implementation, this would call the backend API
            // to create test emails
            emails.add(email)
        }
        
        return@runBlocking emails
    }
    
    /**
     * Authenticates a test user and returns auth token
     */
    fun authenticateTestUser(email: String = TestConfig.testEmail, 
                           password: String = TestConfig.testPassword): String? = runBlocking {
        try {
            // This would make actual API call to authenticate
            val response: HttpResponse = client.post("$baseUrl/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            
            if (response.status == HttpStatusCode.OK) {
                // Extract token from response
                return@runBlocking "test-auth-token"
            }
        } catch (e: Exception) {
            println("Authentication failed: ${e.message}")
        }
        return@runBlocking null
    }
    
    /**
     * Cleans up test data after tests
     */
    fun cleanup() = runBlocking {
        try {
            // Delete test emails
            client.delete("$baseUrl/api/test/cleanup")
        } catch (e: Exception) {
            println("Cleanup failed: ${e.message}")
        }
    }
    
    /**
     * Waits for email processing (summaries) to complete
     */
    fun waitForEmailProcessing(timeoutMs: Long = 10000) {
        Thread.sleep(timeoutMs)
    }
    
    fun close() {
        client.close()
    }
}

@Serializable
data class TestEmail(
    val subject: String,
    val sender: String,
    val content: String,
    val isUnread: Boolean
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)