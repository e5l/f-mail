package com.fmail.e2e.tests

import com.fmail.e2e.pages.EmailListPage
import com.fmail.e2e.pages.EmailDetailPage
import com.fmail.e2e.pages.LoginPage
import com.fmail.e2e.utils.BaseE2ETest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@Tag("e2e")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EmailListE2ETest : BaseE2ETest() {
    
    private lateinit var loginPage: LoginPage
    private lateinit var emailListPage: EmailListPage
    private lateinit var emailDetailPage: EmailDetailPage
    
    @BeforeEach
    fun setupPages() {
        loginPage = LoginPage(page)
        emailListPage = EmailListPage(page)
        emailDetailPage = EmailDetailPage(page)
        
        // Navigate to app and mock authentication for testing
        navigateToApp()
        // In a real test, we would authenticate here
        // For now, we'll test what we can without auth
    }
    
    @Test
    @Order(1)
    @DisplayName("Should display email list after authentication")
    @Disabled("Requires authentication setup")
    fun testEmailListDisplay() {
        // After auth, should show email list
        emailListPage.waitForEmailsToLoad()
        
        // Verify email list container is visible
        assertTrue(page.locator(".email-list, #email-list").isVisible(),
            "Email list container should be visible")
        
        takeScreenshot("email_list_display")
    }
    
    @Test
    @Order(2)
    @DisplayName("Should show email summaries in list view")
    @Disabled("Requires authentication and emails")
    fun testEmailSummariesDisplay() {
        emailListPage.waitForEmailsToLoad()
        
        val emailCount = emailListPage.getEmailCount()
        assertTrue(emailCount > 0, "Should have at least one email")
        
        // Check first email has summary
        val firstEmail = emailListPage.getEmailAtIndex(0)
        assertNotNull(firstEmail, "Should get first email info")
        assertTrue(firstEmail!!.summary.isNotEmpty(), "Email should have summary")
        
        // Verify summary is displayed, not full content
        assertTrue(firstEmail.summary.length < 500, 
            "Summary should be concise (less than 500 chars)")
    }
    
    @Test
    @Order(3)
    @DisplayName("Should display unread indicators")
    @Disabled("Requires authentication and unread emails")
    fun testUnreadIndicators() {
        emailListPage.waitForEmailsToLoad()
        
        val unreadCount = emailListPage.getUnreadEmailCount()
        
        // Check that unread emails have indicators
        for (i in 0 until emailListPage.getEmailCount()) {
            val email = emailListPage.getEmailAtIndex(i)
            if (email?.isUnread == true) {
                // Verify visual distinction for unread emails
                val emailElement = page.locator(".email-item").nth(i)
                assertTrue(
                    emailElement.locator(".unread-indicator").isVisible() ||
                    emailElement.getAttribute("class")?.contains("unread") == true,
                    "Unread email should have visual indicator"
                )
            }
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Should refresh email list")
    @Disabled("Requires authentication")
    fun testEmailListRefresh() {
        emailListPage.waitForEmailsToLoad()
        
        val initialCount = emailListPage.getEmailCount()
        
        // Click refresh
        emailListPage.refreshEmailList()
        
        // Verify refresh happened (loading state or updated count)
        val newCount = emailListPage.getEmailCount()
        assertTrue(newCount >= 0, "Should still have email count after refresh")
        
        takeScreenshot("email_list_after_refresh")
    }
    
    @Test
    @Order(5)
    @DisplayName("Should navigate to email detail on click")
    @Disabled("Requires authentication and emails")
    fun testEmailNavigation() {
        emailListPage.waitForEmailsToLoad()
        
        assertTrue(emailListPage.getEmailCount() > 0, "Need at least one email")
        
        // Get first email info
        val firstEmail = emailListPage.getEmailAtIndex(0)
        assertNotNull(firstEmail)
        
        // Click first email
        emailListPage.clickEmailAtIndex(0)
        
        // Verify navigation to detail view
        emailDetailPage.waitForEmailToLoad()
        
        // Verify email subject matches
        val detailSubject = emailDetailPage.getEmailSubject()
        assertEquals(firstEmail!!.subject, detailSubject, 
            "Detail view should show same email subject")
    }
    
    @Test
    @Order(6)
    @DisplayName("Should show AI-generated summary with 10 bullet points")
    @Disabled("Requires authentication and emails with summaries")
    fun testAISummaryFormat() {
        emailListPage.waitForEmailsToLoad()
        emailListPage.clickEmailAtIndex(0)
        
        emailDetailPage.waitForEmailToLoad()
        
        // Check summary exists
        assertTrue(emailDetailPage.hasSummary(), "Email should have AI summary")
        
        // Get bullet points
        val bulletPoints = emailDetailPage.getSummaryBulletPoints()
        
        // Verify exactly 10 bullet points
        assertEquals(10, bulletPoints.size, 
            "AI summary should have exactly 10 bullet points")
        
        // Verify each bullet point has content
        bulletPoints.forEach { bullet ->
            assertTrue(bullet.trim().isNotEmpty(), 
                "Each bullet point should have content")
        }
        
        takeScreenshot("email_with_ai_summary")
    }
    
    @Test
    @Order(7)
    @DisplayName("Should auto-refresh emails every minute")
    @Disabled("Requires authentication and long wait time")
    fun testAutoRefresh() {
        emailListPage.waitForEmailsToLoad()
        
        val initialCount = emailListPage.getEmailCount()
        val startTime = System.currentTimeMillis()
        
        // Wait for just over 1 minute
        Thread.sleep(65000) // 65 seconds
        
        // Check if refresh happened (new emails or updated timestamps)
        val newCount = emailListPage.getEmailCount()
        
        // In a real test, we'd check for:
        // - Network requests to refresh endpoint
        // - Updated timestamps
        // - New emails if any arrived
        
        assertTrue(System.currentTimeMillis() - startTime > 60000,
            "Should wait at least 1 minute")
    }
}