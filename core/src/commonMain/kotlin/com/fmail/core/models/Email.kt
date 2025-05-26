package com.fmail.core.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Email(
    val id: String,
    val messageId: String,
    val subject: String,
    val from: EmailAddress,
    val to: List<EmailAddress>,
    val cc: List<EmailAddress> = emptyList(),
    val bcc: List<EmailAddress> = emptyList(),
    val date: Instant,
    val htmlBody: String? = null,
    val textBody: String? = null,
    val summary: EmailSummary? = null,
    val labels: List<String> = emptyList(),
    val isRead: Boolean = false,
    val isStarred: Boolean = false,
    val attachments: List<Attachment> = emptyList()
)

@Serializable
data class EmailAddress(
    val email: String,
    val name: String? = null
)

@Serializable
data class EmailSummary(
    val bulletPoints: List<String>,
    val generatedAt: Instant
)

@Serializable
data class Attachment(
    val filename: String,
    val mimeType: String,
    val size: Long
)