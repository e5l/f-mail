package com.fmail.core.domain.email.entities

import com.fmail.core.domain.email.valueobjects.EmailAddress
import com.fmail.core.domain.email.valueobjects.EmailId
import kotlinx.datetime.Instant

data class Email(
    val id: EmailId,
    val from: EmailAddress,
    val to: List<EmailAddress>,
    val cc: List<EmailAddress> = emptyList(),
    val bcc: List<EmailAddress> = emptyList(),
    val subject: String,
    val body: String,
    val timestamp: Instant,
    val isRead: Boolean,
    val labels: Set<String>,
    val summary: EmailSummary? = null
) {
    init {
        require(to.isNotEmpty()) { "Email must have at least one recipient" }
    }

    fun markAsRead(): Email = copy(isRead = true)
    
    fun markAsUnread(): Email = copy(isRead = false)
    
    fun addLabel(label: String): Email = copy(labels = labels + label)
    
    fun removeLabel(label: String): Email = copy(labels = labels - label)
    
    fun archive(): Email = copy(
        labels = (labels - "Inbox") + "Archive"
    )
    
    fun withSummary(points: List<String>): Email = copy(
        summary = EmailSummary(
            emailId = id,
            points = points,
            generatedAt = kotlinx.datetime.Clock.System.now()
        )
    )
}