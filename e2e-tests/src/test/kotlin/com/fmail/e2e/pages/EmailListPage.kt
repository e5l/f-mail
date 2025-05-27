package com.fmail.e2e.pages

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.LoadState
import com.microsoft.playwright.options.WaitForSelectorState

class EmailListPage(private val page: Page) {
    
    // Selectors
    private val emailListContainer = ".email-list, #email-list, [data-testid='email-list']"
    private val emailItem = ".email-item, .email-row, [data-testid='email-item']"
    private val emailSubject = ".email-subject, .subject"
    private val emailSummary = ".email-summary, .summary"
    private val emailSender = ".email-sender, .from"
    private val emailDate = ".email-date, .date"
    private val unreadIndicator = ".unread-indicator, .unread"
    private val refreshButton = "button:has-text('Refresh'), button[aria-label='Refresh']"
    private val loadingSpinner = ".loading, .spinner, [data-testid='loading']"
    
    fun waitForEmailsToLoad() {
        page.waitForSelector(emailListContainer)
        // Wait for loading to complete
        if (page.locator(loadingSpinner).isVisible()) {
            page.waitForSelector(loadingSpinner, Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED))
        }
    }
    
    fun getEmailCount(): Int {
        waitForEmailsToLoad()
        return page.locator(emailItem).count()
    }
    
    fun getEmailAtIndex(index: Int): EmailItemInfo? {
        val emails = page.locator(emailItem)
        if (index >= emails.count()) return null
        
        val emailLocator = emails.nth(index)
        return EmailItemInfo(
            subject = emailLocator.locator(emailSubject).textContent() ?: "",
            summary = emailLocator.locator(emailSummary).textContent() ?: "",
            sender = emailLocator.locator(emailSender).textContent() ?: "",
            date = emailLocator.locator(emailDate).textContent() ?: "",
            isUnread = emailLocator.locator(unreadIndicator).isVisible()
        )
    }
    
    fun clickEmailAtIndex(index: Int) {
        val emails = page.locator(emailItem)
        if (index < emails.count()) {
            emails.nth(index).click()
            page.waitForLoadState(LoadState.NETWORKIDLE)
        }
    }
    
    fun searchEmailBySubject(subject: String): Locator? {
        val emails = page.locator(emailItem)
        for (i in 0 until emails.count()) {
            val email = emails.nth(i)
            if (email.locator(emailSubject).textContent()?.contains(subject) == true) {
                return email
            }
        }
        return null
    }
    
    fun clickEmailBySubject(subject: String): Boolean {
        val email = searchEmailBySubject(subject)
        return if (email != null) {
            email.click()
            page.waitForLoadState(LoadState.NETWORKIDLE)
            true
        } else {
            false
        }
    }
    
    fun refreshEmailList() {
        page.click(refreshButton)
        waitForEmailsToLoad()
    }
    
    fun getUnreadEmailCount(): Int {
        waitForEmailsToLoad()
        return page.locator("$emailItem:has($unreadIndicator)").count()
    }
    
    fun getAllEmailSummaries(): List<String> {
        waitForEmailsToLoad()
        return page.locator("$emailItem $emailSummary").allTextContents()
    }
    
    fun waitForNewEmails(initialCount: Int, timeout: Long = 5000) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeout) {
            if (getEmailCount() > initialCount) {
                return
            }
            Thread.sleep(500)
        }
    }
    
    data class EmailItemInfo(
        val subject: String,
        val summary: String,
        val sender: String,
        val date: String,
        val isUnread: Boolean
    )
}