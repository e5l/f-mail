package com.fmail.core.domain.email.entities

import com.fmail.core.domain.email.valueobjects.EmailAddress
import com.fmail.core.domain.email.valueobjects.EmailId
import kotlinx.datetime.Clock
import kotlin.test.*

class EmailTest {
    private val emailId = EmailId("test-123")
    private val from = EmailAddress("sender@example.com")
    private val to = listOf(EmailAddress("recipient@example.com"))
    private val subject = "Test Subject"
    private val body = "Test body content"
    private val timestamp = Clock.System.now()

    @Test
    fun `should create email with valid data`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = false,
            labels = emptySet()
        )

        assertEquals(emailId, email.id)
        assertEquals(from, email.from)
        assertEquals(to, email.to)
        assertEquals(subject, email.subject)
        assertEquals(body, email.body)
        assertEquals(timestamp, email.timestamp)
        assertFalse(email.isRead)
        assertTrue(email.labels.isEmpty())
    }

    @Test
    fun `should mark email as read`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = false,
            labels = emptySet()
        )

        val readEmail = email.markAsRead()
        
        assertTrue(readEmail.isRead)
        assertEquals(email.id, readEmail.id)
    }

    @Test
    fun `should mark email as unread`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = true,
            labels = emptySet()
        )

        val unreadEmail = email.markAsUnread()
        
        assertFalse(unreadEmail.isRead)
        assertEquals(email.id, unreadEmail.id)
    }

    @Test
    fun `should add label to email`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = false,
            labels = emptySet()
        )

        val labeledEmail = email.addLabel("Important")
        
        assertTrue(labeledEmail.labels.contains("Important"))
        assertEquals(1, labeledEmail.labels.size)
    }

    @Test
    fun `should remove label from email`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = false,
            labels = setOf("Important", "Work")
        )

        val updatedEmail = email.removeLabel("Important")
        
        assertFalse(updatedEmail.labels.contains("Important"))
        assertTrue(updatedEmail.labels.contains("Work"))
        assertEquals(1, updatedEmail.labels.size)
    }

    @Test
    fun `should archive email`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = false,
            labels = setOf("Inbox")
        )

        val archivedEmail = email.archive()
        
        assertFalse(archivedEmail.labels.contains("Inbox"))
        assertTrue(archivedEmail.labels.contains("Archive"))
    }

    @Test
    fun `should create summary for email`() {
        val email = Email(
            id = emailId,
            from = from,
            to = to,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = false,
            labels = emptySet()
        )

        val summaryPoints = listOf(
            "Point 1",
            "Point 2",
            "Point 3"
        )

        val emailWithSummary = email.withSummary(summaryPoints)
        
        assertNotNull(emailWithSummary.summary)
        assertEquals(summaryPoints, emailWithSummary.summary?.points)
        assertEquals(email.id, emailWithSummary.summary?.emailId)
    }
}