# Email Summarization App Specification

## Overview
A simple email client that automatically generates AI-powered summaries for emails using Gmail IMAP, built with Kotlin, Ktor, and Compose Multiplatform for WebAssembly.

## Core Features

### Email Summarization
- **LLM Provider**: Ollama with OpenAI API compatibility
- **Model**: Llama 3.2
- **Summary Format**: 10 bullet points per email
- **Display**: Summary shown by default, original email content displayed below

### Gmail Integration
- **Protocol**: IMAP
- **Authentication**: OAuth2
- **Email Scope**: All unread emails across all folders/labels
- **Sync Frequency**: Automatic refresh every 1 minute

### User Interface
- **Design**: Similar to Apple Mail app interface
- **Email Actions**: 
  - Reply
  - Forward
  - Delete
  - Archive
  - Mark as read/unread
- **Folder Support**: Display all Gmail folders/labels
- **Read Status**: Preserve Gmail's read/unread status, only mark as read when user opens email in app

## Technical Stack
- **Language**: Kotlin
- **Backend Framework**: Ktor
- **Frontend**: Compose Multiplatform (WASM target)
- **Email Protocol**: IMAP
- **AI Integration**: Ollama API (OpenAI-compatible endpoint)

## User Flow
1. User authenticates with Gmail via OAuth2
2. App fetches all unread emails from all folders
3. Each email is sent to Ollama for summarization
4. Emails displayed in list view with summaries
5. Clicking an email shows summary + original content below
6. Standard email actions available for each message
7. Background refresh every minute for new emails

## Non-Functional Requirements
- Simple, single-purpose application
- Focus on email summarization as the primary feature
- Clean, intuitive interface similar to native mail clients