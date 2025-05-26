package com.fmail.core.application.email

import com.fmail.core.domain.email.entities.Email
import com.fmail.core.domain.email.repositories.EmailRepository
import com.fmail.core.domain.email.services.EmailSummaryService
import com.fmail.core.domain.email.valueobjects.EmailAddress
import com.fmail.core.domain.email.valueobjects.EmailId
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FetchEmailsUseCaseTest {
    private val mockRepository = MockEmailRepository()
    private val mockSummaryService = MockEmailSummaryService()
    private val useCase = FetchEmailsUseCase(mockRepository, mockSummaryService)

    @Test
    fun `should fetch all emails with summaries`() = runTest {
        val email1 = createTestEmail("1", "Test 1")
        val email2 = createTestEmail("2", "Test 2")
        mockRepository.emails = listOf(email1, email2)

        val result = useCase.execute()

        assertEquals(2, result.size)
        result.forEach { email ->
            assertNotNull(email.summary)
            assertEquals(3, email.summary?.points?.size)
        }
    }

    @Test
    fun `should fetch unread emails only`() = runTest {
        val email1 = createTestEmail("1", "Test 1", isRead = false)
        val email2 = createTestEmail("2", "Test 2", isRead = true)
        val email3 = createTestEmail("3", "Test 3", isRead = false)
        mockRepository.emails = listOf(email1, email2, email3)

        val result = useCase.execute(unreadOnly = true)

        assertEquals(2, result.size)
        assertEquals("1", result[0].id.value)
        assertEquals("3", result[1].id.value)
    }

    private fun createTestEmail(
        id: String,
        subject: String,
        isRead: Boolean = false
    ) = Email(
        id = EmailId(id),
        from = EmailAddress("sender@example.com"),
        to = listOf(EmailAddress("recipient@example.com")),
        subject = subject,
        body = "Test body",
        timestamp = Clock.System.now(),
        isRead = isRead,
        labels = setOf("Inbox")
    )
}

class MockEmailRepository : EmailRepository {
    var emails = listOf<Email>()

    override suspend fun findById(id: EmailId) = emails.find { it.id == id }
    override suspend fun findAll() = emails
    override suspend fun findByLabel(label: String) = emails.filter { label in it.labels }
    override suspend fun findUnread() = emails.filter { !it.isRead }
    override suspend fun save(email: Email) = email
    override suspend fun delete(id: EmailId) {}
    override suspend fun markAsRead(id: EmailId) {}
    override suspend fun markAsUnread(id: EmailId) {}
    override suspend fun addLabel(id: EmailId, label: String) {}
    override suspend fun removeLabel(id: EmailId, label: String) {}
}

class MockEmailSummaryService : EmailSummaryService {
    override suspend fun generateSummary(email: Email) = listOf(
        "Summary point 1 for ${email.subject}",
        "Summary point 2 for ${email.subject}",
        "Summary point 3 for ${email.subject}"
    )
}