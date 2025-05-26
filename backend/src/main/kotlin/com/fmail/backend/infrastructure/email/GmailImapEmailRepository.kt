package com.fmail.backend.infrastructure.email

import com.fmail.core.domain.email.entities.Email
import com.fmail.core.domain.email.repositories.EmailRepository
import com.fmail.core.domain.email.valueobjects.EmailAddress
import com.fmail.core.domain.email.valueobjects.EmailId
import com.google.api.client.auth.oauth2.Credential
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.ModifyMessageRequest
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock
import java.util.*
import javax.mail.Session
import javax.mail.internet.MimeMessage
import java.io.ByteArrayInputStream
import javax.mail.internet.InternetAddress

class GmailImapEmailRepository(
    private val gmail: Gmail,
    private val userId: String = "me"
) : EmailRepository {
    
    override suspend fun findById(id: EmailId): Email? {
        return try {
            val message = gmail.users().messages().get(userId, id.value).execute()
            messageToEmail(message)
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findAll(): List<Email> {
        val messages = gmail.users().messages().list(userId)
            .setMaxResults(100L)
            .execute()
            .messages ?: return emptyList()
        
        return messages.mapNotNull { messageRef ->
            try {
                val fullMessage = gmail.users().messages().get(userId, messageRef.id).execute()
                messageToEmail(fullMessage)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    override suspend fun findByLabel(label: String): List<Email> {
        val labelId = getLabelId(label) ?: return emptyList()
        
        val messages = gmail.users().messages().list(userId)
            .setLabelIds(listOf(labelId))
            .setMaxResults(100L)
            .execute()
            .messages ?: return emptyList()
        
        return messages.mapNotNull { messageRef ->
            try {
                val fullMessage = gmail.users().messages().get(userId, messageRef.id).execute()
                messageToEmail(fullMessage)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    override suspend fun findUnread(): List<Email> {
        val messages = gmail.users().messages().list(userId)
            .setQ("is:unread")
            .setMaxResults(100L)
            .execute()
            .messages ?: return emptyList()
        
        return messages.mapNotNull { messageRef ->
            try {
                val fullMessage = gmail.users().messages().get(userId, messageRef.id).execute()
                messageToEmail(fullMessage)
            } catch (e: Exception) {
                null
            }
        }
    }
    
    override suspend fun save(email: Email): Email {
        throw UnsupportedOperationException("Cannot create new emails via Gmail API")
    }
    
    override suspend fun delete(id: EmailId) {
        gmail.users().messages().trash(userId, id.value).execute()
    }
    
    override suspend fun markAsRead(id: EmailId) {
        val request = ModifyMessageRequest()
            .setRemoveLabelIds(listOf("UNREAD"))
        gmail.users().messages().modify(userId, id.value, request).execute()
    }
    
    override suspend fun markAsUnread(id: EmailId) {
        val request = ModifyMessageRequest()
            .setAddLabelIds(listOf("UNREAD"))
        gmail.users().messages().modify(userId, id.value, request).execute()
    }
    
    override suspend fun addLabel(id: EmailId, label: String) {
        val labelId = getLabelId(label) ?: createLabel(label)
        val request = ModifyMessageRequest()
            .setAddLabelIds(listOf(labelId))
        gmail.users().messages().modify(userId, id.value, request).execute()
    }
    
    override suspend fun removeLabel(id: EmailId, label: String) {
        val labelId = getLabelId(label) ?: return
        val request = ModifyMessageRequest()
            .setRemoveLabelIds(listOf(labelId))
        gmail.users().messages().modify(userId, id.value, request).execute()
    }
    
    private fun messageToEmail(message: Message): Email? {
        val mimeMessage = getMimeMessage(message) ?: return null
        
        val from = (mimeMessage.from?.firstOrNull() as? InternetAddress)?.address
            ?.let { EmailAddress(it) } ?: return null
            
        val to = mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO)
            ?.mapNotNull { (it as? InternetAddress)?.address }
            ?.map { EmailAddress(it) }
            ?: emptyList()
            
        val cc = mimeMessage.getRecipients(javax.mail.Message.RecipientType.CC)
            ?.mapNotNull { (it as? InternetAddress)?.address }
            ?.map { EmailAddress(it) }
            ?: emptyList()
            
        val subject = mimeMessage.subject ?: ""
        val body = extractBody(mimeMessage)
        val timestamp = message.internalDate?.let { date ->
            Instant.fromEpochMilliseconds(date) 
        } ?: Clock.System.now()
        
        val labels = message.labelIds?.toSet() ?: emptySet()
        val isRead = !labels.contains("UNREAD")
        
        return Email(
            id = EmailId(message.id),
            from = from,
            to = to,
            cc = cc,
            subject = subject,
            body = body,
            timestamp = timestamp,
            isRead = isRead,
            labels = labels.mapNotNull { labelIdToName(it) }.toSet()
        )
    }
    
    private fun getMimeMessage(message: Message): MimeMessage? {
        return try {
            val payload = message.payload
            val raw = message.raw ?: payload?.body?.data
            if (raw != null) {
                val props = Properties()
                val session = Session.getDefaultInstance(props, null)
                val bytes = Base64.getUrlDecoder().decode(raw)
                MimeMessage(session, ByteArrayInputStream(bytes))
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun extractBody(message: MimeMessage): String {
        return try {
            when {
                message.isMimeType("text/plain") -> message.content.toString()
                message.isMimeType("text/html") -> message.content.toString()
                message.isMimeType("multipart/*") -> {
                    val multipart = message.content as javax.mail.Multipart
                    (0 until multipart.count).asSequence()
                        .map { multipart.getBodyPart(it) }
                        .firstOrNull { it.isMimeType("text/plain") || it.isMimeType("text/html") }
                        ?.content?.toString() ?: ""
                }
                else -> ""
            }
        } catch (e: Exception) {
            ""
        }
    }
    
    private fun getLabelId(labelName: String): String? {
        return try {
            gmail.users().labels().list(userId).execute()
                .labels?.find { it.name == labelName }?.id
        } catch (e: Exception) {
            null
        }
    }
    
    private fun createLabel(labelName: String): String {
        val label = com.google.api.services.gmail.model.Label()
            .setName(labelName)
            .setLabelListVisibility("labelShow")
            .setMessageListVisibility("show")
        return gmail.users().labels().create(userId, label).execute().id
    }
    
    private fun labelIdToName(labelId: String): String? {
        return when (labelId) {
            "INBOX" -> "Inbox"
            "SENT" -> "Sent"
            "DRAFT" -> "Drafts"
            "SPAM" -> "Spam"
            "TRASH" -> "Trash"
            "UNREAD" -> null // This is a system label, not a folder
            "STARRED" -> "Starred"
            "IMPORTANT" -> "Important"
            else -> try {
                gmail.users().labels().get(userId, labelId).execute().name
            } catch (e: Exception) {
                null
            }
        }
    }
}