package com.fmail.e2e.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.LoadState

class LoginPage(private val page: Page) {
    
    // Selectors
    private val emailInput = "input[type='email'], input[name='email'], input#email"
    private val passwordInput = "input[type='password'], input[name='password'], input#password"
    private val loginButton = "button:has-text('Login'), button:has-text('Sign in'), button[type='submit']"
    private val googleLoginButton = "button:has-text('Sign in with Google'), button:has-text('Continue with Google')"
    private val errorMessage = ".error-message, .alert-error, [role='alert']"
    
    fun navigateToLogin() {
        page.navigate("${page.context().browser().browserType().name()}/login")
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun enterEmail(email: String) {
        page.fill(emailInput, email)
    }
    
    fun enterPassword(password: String) {
        page.fill(passwordInput, password)
    }
    
    fun clickLogin() {
        page.click(loginButton)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun loginWithCredentials(email: String, password: String) {
        enterEmail(email)
        enterPassword(password)
        clickLogin()
    }
    
    fun clickGoogleLogin() {
        page.click(googleLoginButton)
        // Handle Google OAuth flow in a new window/tab
        val newPage = page.context().waitForPage {
            // This runs when a new page is created
        }
        handleGoogleOAuth(newPage)
    }
    
    private fun handleGoogleOAuth(oauthPage: Page) {
        // Wait for Google login page
        oauthPage.waitForLoadState(LoadState.NETWORKIDLE)
        
        // Fill in Google credentials
        oauthPage.fill("input[type='email']", System.getenv("GOOGLE_TEST_EMAIL") ?: "")
        oauthPage.click("button:has-text('Next')")
        
        oauthPage.waitForSelector("input[type='password']")
        oauthPage.fill("input[type='password']", System.getenv("GOOGLE_TEST_PASSWORD") ?: "")
        oauthPage.click("button:has-text('Next')")
        
        // Wait for redirect back to app
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun isLoggedIn(): Boolean {
        // Check if we're redirected to the main app or if there's a logout button
        return page.url().contains("/emails") || 
               page.locator("button:has-text('Logout'), button:has-text('Sign out')").isVisible()
    }
    
    fun getErrorMessage(): String? {
        return if (page.locator(errorMessage).isVisible()) {
            page.locator(errorMessage).textContent()
        } else null
    }
}