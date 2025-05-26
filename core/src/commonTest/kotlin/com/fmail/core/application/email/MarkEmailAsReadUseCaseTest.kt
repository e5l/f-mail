package com.fmail.core.application.email

import com.fmail.core.domain.email.entities.Email
import com.fmail.core.domain.email.repositories.EmailRepository
import com.fmail.core.domain.email.valueobjects.EmailAddress
import com.fmail.core.domain.email.valueobjects.EmailId
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class MarkEmailAsReadUseCaseTest {
    private val mockRepository = MockEmailRepositoryWithState()
    private val useCase = MarkEmailAsReadUseCase(mockRepository)

    @Test
    fun `should mark email as read`() = runTest {
        val email = createTestEmail("1", isRead = false)
        mockRepository.emails["1"] = email

        useCase.execute(EmailId("1"))

        assertTrue(mockRepository.emails["1"]!!.isRead)
    }

    @Test
    fun `should handle already read email`() = runTest {
        val email = createTestEmail("1", isRead = true)
        mockRepository.emails["1"] = email

        useCase.execute(EmailId("1"))

        assertTrue(mockRepository.emails["1"]!!.isRead)
    }

    @Test
    fun `should throw exception for non-existent email`() = runTest {
        val exception = assertFailsWith<IllegalArgumentException> {
            useCase.execute(EmailId("non-existent"))
        }
        assertTrue(exception.message?.contains("not found") == true)
    }

    private fun createTestEmail(id: String, isRead: Boolean) = Email(
        id = EmailId(id),
        from = EmailAddress("sender@example.com"),
        to = listOf(EmailAddress("recipient@example.com")),
        subject = "Test Subject",
        body = "Test body",
        timestamp = Clock.System.now(),
        isRead = isRead,
        labels = setOf("Inbox")
    )
}

class MockEmailRepositoryWithState : EmailRepository {
    val emails = mutableMapOf<String, Email>()

    override suspend fun findById(id: EmailId) = emails[id.value]
    override suspend fun findAll() = emails.values.toList()
    override suspend fun findByLabel(label: String) = emails.values.filter { label in it.labels }
    override suspend fun findUnread() = emails.values.filter { !it.isRead }
    override suspend fun save(email: Email): Email {
        emails[email.id.value] = email
        return email
    }
    override suspend fun delete(id: EmailId) {
        emails.remove(id.value)
    }
    override suspend fun markAsRead(id: EmailId) {
        emails[id.value]?.let {
            emails[id.value] = it.markAsRead()
        }
    }
    override suspend fun markAsUnread(id: EmailId) {
        emails[id.value]?.let {
            emails[id.value] = it.markAsUnread()
        }
    }
    override suspend fun addLabel(id: EmailId, label: String) {
        emails[id.value]?.let {
            emails[id.value] = it.addLabel(label)
        }
    }
    override suspend fun removeLabel(id: EmailId, label: String) {
        emails[id.value]?.let {
            emails[id.value] = it.removeLabel(label)
        }
    }
}