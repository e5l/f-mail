package com.fmail.frontend.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fmail.core.models.Email
import com.fmail.frontend.ui.components.EmailDetail
import com.fmail.frontend.ui.components.EmailList
import com.fmail.frontend.viewmodels.EmailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel = remember { EmailViewModel() }
    val emails by viewModel.emails.collectAsState()
    val selectedEmail by viewModel.selectedEmail.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadEmails()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("F-Mail") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Email list on the left
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                EmailList(
                    emails = emails,
                    selectedEmail = selectedEmail,
                    onEmailClick = { email ->
                        viewModel.selectEmail(email)
                    }
                )
            }
            
            // Divider
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            // Email detail on the right
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
            ) {
                selectedEmail?.let { email ->
                    EmailDetail(
                        email = email,
                        onReply = { /* TODO */ },
                        onForward = { /* TODO */ },
                        onDelete = { viewModel.deleteEmail(email.id) },
                        onArchive = { viewModel.archiveEmail(email.id) },
                        onToggleRead = { 
                            if (email.isRead) {
                                viewModel.markAsUnread(email.id)
                            } else {
                                viewModel.markAsRead(email.id)
                            }
                        }
                    )
                } ?: Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = "Select an email to view",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}