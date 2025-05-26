package com.fmail.core.domain.email.services

import com.fmail.core.domain.email.entities.Email

interface EmailSummaryService {
    suspend fun generateSummary(email: Email): List<String>
}