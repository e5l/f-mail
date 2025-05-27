# F-Mail E2E Tests

End-to-end integration tests for the F-Mail application using Playwright for browser automation.

## Prerequisites

- Java 11 or higher
- Running F-Mail application (backend + frontend)
- Chrome/Chromium browser

## Setup

1. Install Playwright browsers:
```bash
./gradlew :e2e-tests:installPlaywright
```

2. Start the application:
```bash
# Terminal 1 - Start backend
./gradlew :backend:run

# Terminal 2 - Start frontend (if not served by backend)
./gradlew :frontend:wasmJsBrowserRun
```

## Running Tests

Run all E2E tests:
```bash
./gradlew :e2e-tests:e2eTest
```

Run specific test class:
```bash
./gradlew :e2e-tests:test --tests "*.AuthenticationE2ETest"
```

Run with custom configuration:
```bash
# Run in headless mode
HEADLESS=true ./gradlew :e2e-tests:e2eTest

# Run with slow motion (useful for debugging)
SLOW_MO=500 ./gradlew :e2e-tests:e2eTest

# Use different base URL
BASE_URL=http://localhost:3000 ./gradlew :e2e-tests:e2eTest
```

## Test Structure

```
e2e-tests/
├── src/test/kotlin/com/fmail/e2e/
│   ├── tests/              # Test classes
│   │   ├── AuthenticationE2ETest.kt
│   │   ├── EmailListE2ETest.kt
│   │   └── EmailActionsE2ETest.kt
│   ├── pages/              # Page Object Model
│   │   ├── LoginPage.kt
│   │   ├── EmailListPage.kt
│   │   └── EmailDetailPage.kt
│   └── utils/              # Test utilities
│       ├── BaseE2ETest.kt
│       ├── TestConfig.kt
│       └── TestDataHelper.kt
```

## Test Coverage

### Authentication Tests
- Login page display
- Google OAuth button presence
- OAuth flow (disabled - requires real account)
- Authentication persistence
- Logout functionality
- Error handling

### Email List Tests
- Email list display after auth
- Summary display in list view
- Unread indicators
- List refresh functionality
- Navigation to email details
- AI summary format (10 bullet points)
- Auto-refresh every minute

### Email Actions Tests
- Mark as read when opened
- Mark as unread
- Delete email
- Archive email
- Reply to email
- Forward email
- Gmail status synchronization

## Environment Variables

- `HEADLESS`: Run browser in headless mode (default: false)
- `BASE_URL`: Application URL (default: http://localhost:8080)
- `SLOW_MO`: Slow down operations by specified milliseconds (default: 0)
- `TEST_EMAIL`: Test user email
- `TEST_PASSWORD`: Test user password
- `GOOGLE_TEST_EMAIL`: Google account for OAuth testing
- `GOOGLE_TEST_PASSWORD`: Google account password

## Screenshots

Failed tests automatically capture screenshots to `build/test-results/screenshots/`.

## Notes

- Many tests are currently `@Disabled` as they require authentication setup
- Real OAuth testing requires test Google accounts and proper configuration
- Consider using mock authentication for CI/CD environments
- Tests assume the application is running and accessible

## Future Improvements

1. Mock authentication for testing without real Google accounts
2. Test data seeding via API
3. Parallel test execution
4. Better test isolation
5. CI/CD integration
6. Performance testing
7. Visual regression testing