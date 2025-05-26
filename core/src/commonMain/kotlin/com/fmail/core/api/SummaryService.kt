package com.fmail.core.api

interface SummaryService {
    suspend fun generateSummary(emailContent: String): List<String>
}