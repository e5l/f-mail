# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an email summarization application that uses Gmail IMAP and AI to automatically generate summaries for emails. The project is built with:
- **Kotlin** as the primary language
- **Ktor** for the backend server
- **Compose Multiplatform** targeting WebAssembly for the frontend
- **Gmail IMAP** for email access with OAuth2 authentication
- **Ollama API** (OpenAI-compatible) with Llama 3.2 model for email summarization

## Key Architecture Components

### Email Processing Flow
1. Gmail OAuth2 authentication
2. IMAP connection to fetch unread emails from all folders/labels
3. Each email sent to Ollama for 10-bullet-point summarization
4. Background refresh every 1 minute

### UI Requirements
- Apple Mail-like interface design
- Email list view with summaries displayed by default
- Full email content shown below summary when selected
- Standard email actions: Reply, Forward, Delete, Archive, Mark read/unread
- Preserve Gmail's read/unread status (only mark as read when opened in app)

## Development Commands

Since this is a new project, the following commands will need to be set up:
- **Build**: TBD (likely `./gradlew build` for Kotlin/Gradle project)
- **Run backend**: TBD (likely `./gradlew run` or similar for Ktor)
- **Run frontend**: TBD (Compose Multiplatform WASM compilation)
- **Tests**: TBD (likely `./gradlew test`)
- **Lint**: TBD (likely using ktlint or detekt)

## Important Implementation Notes

- Focus on email summarization as the primary feature
- Maintain simplicity - this is a single-purpose application
- Ensure OAuth2 tokens and credentials are properly secured
- IMAP connection should handle all Gmail folders/labels
- Summary format must be exactly 10 bullet points per email