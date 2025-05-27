package com.fmail.e2e.pages

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.LoadState

class EmailDetailPage(private val page: Page) {
    
    // Selectors
    private val emailContent = ".email-content, .email-body, [data-testid='email-content']"
    private val emailSubject = ".email-detail-subject, h1, [data-testid='email-subject']"
    private val emailSender = ".email-detail-sender, .sender-info, [data-testid='email-sender']"
    private val emailDate = ".email-detail-date, .email-timestamp, [data-testid='email-date']"
    private val emailSummary = ".email-detail-summary, .summary-section, [data-testid='email-summary']"
    private val summaryBulletPoints = ".summary-bullet, .bullet-point, li"
    
    // Action buttons
    private val replyButton = "button:has-text('Reply'), button[aria-label='Reply']"
    private val forwardButton = "button:has-text('Forward'), button[aria-label='Forward']"
    private val deleteButton = "button:has-text('Delete'), button[aria-label='Delete']"
    private val archiveButton = "button:has-text('Archive'), button[aria-label='Archive']"
    private val markUnreadButton = "button:has-text('Mark as unread'), button[aria-label='Mark as unread']"
    private val backButton = "button:has-text('Back'), button[aria-label='Back to list']"
    
    // Compose elements
    private val composeArea = ".compose-area, .reply-area, [data-testid='compose']"
    private val composeTextarea = "textarea, [contenteditable='true']"
    private val sendButton = "button:has-text('Send'), button[aria-label='Send']"
    private val cancelButton = "button:has-text('Cancel'), button[aria-label='Cancel']"
    
    fun waitForEmailToLoad() {
        page.waitForSelector(emailContent)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun getEmailSubject(): String {
        waitForEmailToLoad()
        return page.locator(emailSubject).textContent() ?: ""
    }
    
    fun getEmailSender(): String {
        return page.locator(emailSender).textContent() ?: ""
    }
    
    fun getEmailDate(): String {
        return page.locator(emailDate).textContent() ?: ""
    }
    
    fun getEmailContent(): String {
        return page.locator(emailContent).textContent() ?: ""
    }
    
    fun getSummaryBulletPoints(): List<String> {
        waitForEmailToLoad()
        val summary = page.locator(emailSummary)
        if (!summary.isVisible()) return emptyList()
        
        return summary.locator(summaryBulletPoints).allTextContents()
    }
    
    fun clickReply() {
        page.click(replyButton)
        page.waitForSelector(composeArea)
    }
    
    fun composeReply(message: String) {
        clickReply()
        page.fill(composeTextarea, message)
    }
    
    fun sendReply() {
        page.click(sendButton)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun clickForward() {
        page.click(forwardButton)
        page.waitForSelector(composeArea)
    }
    
    fun deleteEmail() {
        page.click(deleteButton)
        // Handle confirmation dialog if present
        page.onDialog { dialog ->
            dialog.accept()
        }
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun archiveEmail() {
        page.click(archiveButton)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun markAsUnread() {
        page.click(markUnreadButton)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun goBackToList() {
        page.click(backButton)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    fun isReplyAreaVisible(): Boolean {
        return page.locator(composeArea).isVisible()
    }
    
    fun cancelCompose() {
        if (isReplyAreaVisible()) {
            page.click(cancelButton)
        }
    }
    
    fun hasSummary(): Boolean {
        return page.locator(emailSummary).isVisible()
    }
    
    fun getSummaryBulletCount(): Int {
        return if (hasSummary()) {
            page.locator("$emailSummary $summaryBulletPoints").count()
        } else {
            0
        }
    }
}