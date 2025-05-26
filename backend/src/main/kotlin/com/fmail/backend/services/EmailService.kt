package com.fmail.backend.services

import com.fmail.core.api.EmailRepository
import com.fmail.core.models.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EmailService : EmailRepository {
    private val emailsFlow = MutableStateFlow<List<Email>>(emptyList())
    
    override suspend fun fetchEmails(folder: String?): List<Email> {
        // TODO: Implement IMAP email fetching
        return emptyList()
    }
    
    override suspend fun getEmail(id: String): Email? {
        return emailsFlow.value.find { it.id == id }
    }
    
    override suspend fun markAsRead(id: String) {
        // TODO: Implement IMAP mark as read
    }
    
    override suspend fun markAsUnread(id: String) {
        // TODO: Implement IMAP mark as unread
    }
    
    override suspend fun deleteEmail(id: String) {
        // TODO: Implement IMAP delete
    }
    
    override suspend fun archiveEmail(id: String) {
        // TODO: Implement IMAP archive
    }
    
    override suspend fun starEmail(id: String) {
        // TODO: Implement IMAP star
    }
    
    override suspend fun unstarEmail(id: String) {
        // TODO: Implement IMAP unstar
    }
    
    override fun observeEmails(): Flow<List<Email>> {
        return emailsFlow.asStateFlow()
    }
}