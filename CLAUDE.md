# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an email summarization application that uses Gmail IMAP and AI to automatically generate summaries for emails. The project is built with:
- **Kotlin** as the primary language
- **Ktor** for the backend server
- **Compose Multiplatform** targeting WebAssembly for the frontend
- **Gmail IMAP** for email access with OAuth2 authentication
- **Ollama API** (OpenAI-compatible) with Llama 3.2 model for email summarization

## Architecture: Domain-Driven Design (DDD)

The project follows Domain-Driven Design principles with clear separation of concerns:

### Bounded Contexts
1. **Email Management** - Core email operations (read, delete, archive)
2. **Authentication** - OAuth2 and user session management
3. **Summary Generation** - AI-powered email summarization
4. **IMAP Integration** - Gmail connectivity and sync

### Layer Structure
- **Domain Layer** (`core/domain/`) - Business logic, entities, value objects, domain services
  - Entities: `Email`, `EmailSummary`
  - Value Objects: `EmailId`, `EmailAddress`
  - Domain Services: `EmailSummaryService`
  - Repositories: `EmailRepository` (interface)
  
- **Application Layer** (`core/application/`) - Use cases and application services
  - Use Cases: `FetchEmailsUseCase`, `MarkEmailAsReadUseCase`
  - Orchestrates domain objects to fulfill business requirements
  
- **Infrastructure Layer** (`backend/infrastructure/`) - External integrations
  - `GmailImapEmailRepository` - Gmail API implementation
  - `OllamaEmailSummaryService` - Ollama AI integration
  
- **Presentation Layer** (`frontend/`, `backend/routes/`) - UI and REST APIs

## Development Approach: Test-Driven Development (TDD)

### Testing Strategy
1. **Write tests first** - Always create tests before implementing functionality
2. **Domain tests** are in `core/src/commonTest/` - Test business logic in isolation
3. **Use mock implementations** for testing use cases
4. **Red-Green-Refactor cycle** - Write failing test, make it pass, then refactor

### Test Structure
```kotlin
// Example: EmailIdTest.kt
class EmailIdTest {
    @Test
    fun `should create valid EmailId`() { ... }
    
    @Test
    fun `should throw exception for empty id`() { ... }
}
```

### TDD Workflow (MUST FOLLOW)
1. **Write failing test first** - Create test that defines expected behavior
2. **Run test to verify it fails** - `./gradlew :core:jvmTest` (Red phase)
3. **Write minimal code to pass** - Implement just enough to make test green
4. **Run test to verify it passes** - `./gradlew :core:jvmTest` (Green phase)
5. **Refactor if needed** - Clean up code while keeping tests green
6. **Run all tests** - Ensure no regressions with `./gradlew :core:jvmTest :backend:test`

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

- **Build**: `./gradlew build`
- **Run backend**: `./gradlew :backend:run`
- **Run frontend**: `./gradlew :frontend:wasmJsBrowserRun`
- **Run all tests**: `./gradlew :core:jvmTest`
- **Run specific module tests**: `./gradlew :core:jvmTest :backend:test`
- **Lint**: TBD (likely using ktlint or detekt)

## MANDATORY: Compilation and Test Verification

**IMPORTANT**: After making ANY code changes, you MUST:

1. **Verify compilation**: Run `./gradlew clean build` to ensure all modules compile
2. **Run tests**: Execute `./gradlew :core:jvmTest :backend:test` to verify tests pass
3. **Fix any issues**: If compilation or tests fail, fix them immediately before proceeding
4. **Document failures**: If tests are expected to fail (e.g., during TDD red phase), explain why

This is NON-NEGOTIABLE. Every code change must be validated through compilation and tests.

## Important Implementation Notes

- Focus on email summarization as the primary feature
- Maintain simplicity - this is a single-purpose application
- Ensure OAuth2 tokens and credentials are properly secured
- IMAP connection should handle all Gmail folders/labels
- Summary format must be exactly 10 bullet points per email

## DDD Best Practices for this Project

1. **Keep domain logic pure** - No framework dependencies in domain layer
2. **Use value objects** for type safety (EmailId, EmailAddress)
3. **Aggregate boundaries** - Email is the main aggregate root
4. **Domain events** - Consider adding for email state changes
5. **Repository pattern** - Abstract data access behind interfaces
6. **Use cases** represent single business operations
7. **Infrastructure concerns** stay in infrastructure layer only