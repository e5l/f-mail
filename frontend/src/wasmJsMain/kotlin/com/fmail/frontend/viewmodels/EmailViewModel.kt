package com.fmail.frontend.viewmodels

import com.fmail.core.models.Email
import com.fmail.frontend.api.EmailApiClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EmailViewModel {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val apiClient = EmailApiClient()
    
    private val _emails = MutableStateFlow<List<Email>>(emptyList())
    val emails: StateFlow<List<Email>> = _emails.asStateFlow()
    
    private val _selectedEmail = MutableStateFlow<Email?>(null)
    val selectedEmail: StateFlow<Email?> = _selectedEmail.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // Start periodic refresh
        scope.launch {
            while (isActive) {
                loadEmails()
                delay(60_000) // Refresh every minute
            }
        }
    }
    
    fun loadEmails() {
        scope.launch {
            _isLoading.value = true
            try {
                val fetchedEmails = apiClient.fetchEmails()
                _emails.value = fetchedEmails
            } catch (e: Exception) {
                // TODO: Handle error
                println("Error loading emails: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectEmail(email: Email) {
        _selectedEmail.value = email
        if (!email.isRead) {
            markAsRead(email.id)
        }
    }
    
    fun markAsRead(emailId: String) {
        scope.launch {
            try {
                apiClient.markAsRead(emailId)
                updateEmailInList(emailId) { it.copy(isRead = true) }
            } catch (e: Exception) {
                println("Error marking email as read: ${e.message}")
            }
        }
    }
    
    fun markAsUnread(emailId: String) {
        scope.launch {
            try {
                apiClient.markAsUnread(emailId)
                updateEmailInList(emailId) { it.copy(isRead = false) }
            } catch (e: Exception) {
                println("Error marking email as unread: ${e.message}")
            }
        }
    }
    
    fun deleteEmail(emailId: String) {
        scope.launch {
            try {
                apiClient.deleteEmail(emailId)
                _emails.value = _emails.value.filter { it.id != emailId }
                if (_selectedEmail.value?.id == emailId) {
                    _selectedEmail.value = null
                }
            } catch (e: Exception) {
                println("Error deleting email: ${e.message}")
            }
        }
    }
    
    fun archiveEmail(emailId: String) {
        scope.launch {
            try {
                apiClient.archiveEmail(emailId)
                _emails.value = _emails.value.filter { it.id != emailId }
                if (_selectedEmail.value?.id == emailId) {
                    _selectedEmail.value = null
                }
            } catch (e: Exception) {
                println("Error archiving email: ${e.message}")
            }
        }
    }
    
    private fun updateEmailInList(emailId: String, update: (Email) -> Email) {
        _emails.value = _emails.value.map { email ->
            if (email.id == emailId) update(email) else email
        }
        _selectedEmail.value?.let { selected ->
            if (selected.id == emailId) {
                _selectedEmail.value = update(selected)
            }
        }
    }
}