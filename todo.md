# TODO: Email Summarization App

## Implementation Approach
**IMPORTANT**: Focus on getting early results first. The MVP should only include:
1. Gmail OAuth2 login
2. Fetching and displaying email list
3. Showing AI-generated email summaries

All other features (reply, forward, delete, archive, mark read/unread, folder support, auto-refresh) are SECONDARY and should be delayed until the core functionality is working.

## Phase 1: MVP - Core Functionality Only

### 1. Project Setup
- [ ] Initialize Kotlin project with Gradle
- [ ] Set up Ktor server module
- [ ] Set up Compose Multiplatform module with WASM target
- [ ] Add required dependencies (ktor-client, kotlinx-serialization, etc.)

### 2. Backend - Authentication
- [ ] Implement Gmail OAuth2 flow endpoint
- [ ] Set up OAuth2 redirect handling
- [ ] Store access tokens (in-memory for MVP)
- [ ] Create simple auth middleware

### 3. Backend - Email Fetching
- [ ] Implement IMAP connection to Gmail
- [ ] Create endpoint to fetch unread emails (basic fields only: from, subject, date, body)
- [ ] Return emails as JSON to frontend

### 4. Backend - AI Summarization
- [ ] Set up Ollama API client (OpenAI-compatible)
- [ ] Create summarization endpoint
- [ ] Implement prompt for 10-bullet-point summaries
- [ ] Cache summaries in-memory

### 5. Frontend - Basic UI
- [ ] Create login screen with "Sign in with Gmail" button
- [ ] Implement email list view (simple list, no fancy styling)
- [ ] Display email summaries in the list
- [ ] Show full email content when clicked

### 6. Integration
- [ ] Connect frontend to backend endpoints
- [ ] Test end-to-end flow
- [ ] Basic error handling

## Phase 2: Enhanced Features (DELAYED - Do not implement until Phase 1 is complete)

### UI Polish
- [ ] Apple Mail-like interface styling
- [ ] Proper layout and typography
- [ ] Loading states and animations

### Email Actions
- [ ] Reply functionality
- [ ] Forward functionality
- [ ] Delete functionality
- [ ] Archive functionality
- [ ] Mark as read/unread

### Advanced Features
- [ ] Display all Gmail folders/labels
- [ ] Preserve Gmail read/unread status
- [ ] Auto-refresh every 1 minute
- [ ] Proper token refresh handling
- [ ] Persistent storage for tokens and summaries

## Phase 3: Production Ready (DELAYED)
- [ ] Proper error handling and recovery
- [ ] Performance optimizations
- [ ] Security hardening
- [ ] Deployment configuration
- [ ] Documentation

## Notes
- Start with hardcoded OAuth2 credentials for development
- Use in-memory storage initially, add persistence later
- Keep the UI extremely simple in Phase 1
- Test with a small number of emails first