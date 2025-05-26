package com.fmail.core.domain.email.entities

import com.fmail.core.domain.email.valueobjects.EmailId
import kotlinx.datetime.Instant

data class EmailSummary(
    val emailId: EmailId,
    val points: List<String>,
    val generatedAt: Instant
) {
    init {
        require(points.isNotEmpty()) { "Summary must contain at least one point" }
        require(points.size <= 10) { "Summary cannot contain more than 10 points" }
    }
}