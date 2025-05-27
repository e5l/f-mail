package com.fmail.e2e.tests

import com.fmail.e2e.utils.BaseE2ETest
import com.fmail.e2e.utils.TestConfig
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@Tag("e2e")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AppLoadingE2ETest : BaseE2ETest() {
    
    @Test
    @Order(1)
    @DisplayName("Should load the application")
    fun testAppLoads() {
        navigateToApp()
        
        // Verify page loads
        assertEquals(TestConfig.baseUrl + "/", page.url(), "Should load at base URL")
        
        // Check for basic page structure
        assertTrue(page.locator("body").isVisible(), "Body element should be visible")
        
        // Check title
        assertEquals("F-Mail", page.title(), "Page title should be F-Mail")
        
        takeScreenshot("app_loaded")
    }
    
    @Test
    @Order(2)
    @DisplayName("Should have ComposeTarget element for Compose UI")
    fun testComposeTargetExists() {
        navigateToApp()
        
        // Check for Compose target div
        val composeTarget = page.locator("#ComposeTarget")
        assertTrue(composeTarget.count() > 0, "ComposeTarget element should exist")
        
        // Wait a bit for WASM to load
        Thread.sleep(2000)
        
        // Check if any content has been rendered
        val bodyContent = page.locator("body").innerHTML()
        assertTrue(bodyContent.isNotEmpty(), "Page should have content")
        
        takeScreenshot("compose_target_loaded")
    }
    
    @Test
    @Order(3)
    @DisplayName("Should load frontend JavaScript files")
    fun testFrontendResourcesLoad() {
        navigateToApp()
        
        // Check for console errors
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { msg ->
            if (msg.type() == "error") {
                consoleErrors.add(msg.text())
            }
        }
        
        // Wait for page to fully load
        page.waitForLoadState()
        Thread.sleep(1000)
        
        // Check that fmail.js loads (or similar main JS file)
        val scripts = page.locator("script")
        val scriptCount = scripts.count()
        
        println("Found $scriptCount script tags")
        println("Console errors: $consoleErrors")
        
        // The app should load without critical errors
        val criticalErrors = consoleErrors.filter { 
            it.contains("Failed to load resource") && it.contains("fmail.js")
        }
        
        assertTrue(criticalErrors.isEmpty(), 
            "Should not have critical resource loading errors: $criticalErrors")
    }
    
    @Test
    @Order(4) 
    @DisplayName("Should display app content after WASM loads")
    @Disabled("Requires full frontend implementation")
    fun testAppContentDisplays() {
        navigateToApp()
        
        // Wait for WASM and app to initialize
        Thread.sleep(5000)
        
        // Look for any app-specific content
        // This would check for email list, navigation, etc once implemented
        val appContent = page.locator(".email-list, .app-container, [data-testid='app-content']")
        
        assertTrue(appContent.count() > 0, 
            "Should display app content after initialization")
    }
}