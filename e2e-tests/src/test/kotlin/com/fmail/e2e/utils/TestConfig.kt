package com.fmail.e2e.utils

object TestConfig {
    val baseUrl: String = System.getenv("BASE_URL") ?: "http://localhost:8080"
    val headless: Boolean = System.getenv("HEADLESS")?.toBoolean() ?: false
    val slowMo: Double = System.getenv("SLOW_MO")?.toDouble() ?: 0.0
    val timeout: Double = 30000.0 // 30 seconds default timeout
    val navigationTimeout: Double = 60000.0 // 60 seconds for navigation
    
    // Test user credentials (should be configured via environment variables in CI)
    val testEmail: String = System.getenv("TEST_EMAIL") ?: "test@example.com"
    val testPassword: String = System.getenv("TEST_PASSWORD") ?: "testpassword"
    
    // Screenshot settings
    val screenshotOnFailure: Boolean = true
    val screenshotPath: String = "build/test-results/screenshots"
}