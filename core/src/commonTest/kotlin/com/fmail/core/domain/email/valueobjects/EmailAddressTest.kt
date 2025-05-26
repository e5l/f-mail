package com.fmail.core.domain.email.valueobjects

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EmailAddressTest {
    @Test
    fun `should create valid email address`() {
        val email = EmailAddress("user@example.com")
        assertEquals("user@example.com", email.value)
    }

    @Test
    fun `should throw exception for invalid email format`() {
        assertFailsWith<IllegalArgumentException> {
            EmailAddress("invalid-email")
        }
    }

    @Test
    fun `should throw exception for empty email`() {
        assertFailsWith<IllegalArgumentException> {
            EmailAddress("")
        }
    }

    @Test
    fun `should accept complex valid emails`() {
        val validEmails = listOf(
            "user+tag@example.com",
            "first.last@subdomain.example.com",
            "test123@example.co.uk"
        )
        
        validEmails.forEach { email ->
            val address = EmailAddress(email)
            assertEquals(email, address.value)
        }
    }

    @Test
    fun `should provide domain extraction`() {
        val email = EmailAddress("user@example.com")
        assertEquals("example.com", email.domain)
    }

    @Test
    fun `should provide local part extraction`() {
        val email = EmailAddress("user@example.com")
        assertEquals("user", email.localPart)
    }
}