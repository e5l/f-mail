package com.fmail.core.application.email

import com.fmail.core.domain.email.entities.Email
import com.fmail.core.domain.email.repositories.EmailRepository
import com.fmail.core.domain.email.services.EmailSummaryService

class FetchEmailsUseCase(
    private val emailRepository: EmailRepository,
    private val summaryService: EmailSummaryService
) {
    suspend fun execute(unreadOnly: Boolean = false): List<Email> {
        val emails = if (unreadOnly) {
            emailRepository.findUnread()
        } else {
            emailRepository.findAll()
        }

        return emails.map { email ->
            if (email.summary == null) {
                val summaryPoints = summaryService.generateSummary(email)
                email.withSummary(summaryPoints)
            } else {
                email
            }
        }
    }
}