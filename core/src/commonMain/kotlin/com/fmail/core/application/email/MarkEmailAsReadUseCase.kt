package com.fmail.core.application.email

import com.fmail.core.domain.email.repositories.EmailRepository
import com.fmail.core.domain.email.valueobjects.EmailId

class MarkEmailAsReadUseCase(
    private val emailRepository: EmailRepository
) {
    suspend fun execute(emailId: EmailId) {
        val email = emailRepository.findById(emailId)
            ?: throw IllegalArgumentException("Email with ID ${emailId.value} not found")
        
        emailRepository.markAsRead(emailId)
    }
}