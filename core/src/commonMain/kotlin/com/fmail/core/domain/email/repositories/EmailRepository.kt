package com.fmail.core.domain.email.repositories

import com.fmail.core.domain.email.entities.Email
import com.fmail.core.domain.email.valueobjects.EmailId

interface EmailRepository {
    suspend fun findById(id: EmailId): Email?
    suspend fun findAll(): List<Email>
    suspend fun findByLabel(label: String): List<Email>
    suspend fun findUnread(): List<Email>
    suspend fun save(email: Email): Email
    suspend fun delete(id: EmailId)
    suspend fun markAsRead(id: EmailId)
    suspend fun markAsUnread(id: EmailId)
    suspend fun addLabel(id: EmailId, label: String)
    suspend fun removeLabel(id: EmailId, label: String)
}