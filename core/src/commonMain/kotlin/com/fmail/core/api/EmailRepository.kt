package com.fmail.core.api

import com.fmail.core.models.Email
import kotlinx.coroutines.flow.Flow

interface EmailRepository {
    suspend fun fetchEmails(folder: String? = null): List<Email>
    suspend fun getEmail(id: String): Email?
    suspend fun markAsRead(id: String)
    suspend fun markAsUnread(id: String)
    suspend fun deleteEmail(id: String)
    suspend fun archiveEmail(id: String)
    suspend fun starEmail(id: String)
    suspend fun unstarEmail(id: String)
    fun observeEmails(): Flow<List<Email>>
}