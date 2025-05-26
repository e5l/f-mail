package com.fmail.frontend.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fmail.core.models.Email

@Composable
fun EmailDetail(
    email: Email,
    onReply: () -> Unit,
    onForward: () -> Unit,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    onToggleRead: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Action bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onReply) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Reply")
            }
            IconButton(onClick = onForward) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
            IconButton(onClick = onArchive) {
                Icon(Icons.Default.AccountBox, contentDescription = "Archive")
            }
            IconButton(onClick = onToggleRead) {
                Icon(
                    if (email.isRead) Icons.Default.Email else Icons.Default.Email,
                    contentDescription = if (email.isRead) "Mark as unread" else "Mark as read"
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        
        // Email content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Subject
            Text(
                text = email.subject,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // From
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "From: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${email.from.name ?: ""} <${email.from.email}>",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // To
            if (email.to.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "To: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = email.to.joinToString(", ") { "${it.name ?: ""} <${it.email}>" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Date: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email.date.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Summary
            email.summary?.let { summary ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AI Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        summary.bulletPoints.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Email body
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = email.textBody ?: email.htmlBody ?: "No content",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}