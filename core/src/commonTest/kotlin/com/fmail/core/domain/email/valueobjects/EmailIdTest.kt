package com.fmail.core.domain.email.valueobjects

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class EmailIdTest {
    @Test
    fun `should create valid EmailId`() {
        val id = EmailId("123456")
        assertEquals("123456", id.value)
    }

    @Test
    fun `should throw exception for empty id`() {
        assertFailsWith<IllegalArgumentException> {
            EmailId("")
        }
    }

    @Test
    fun `should throw exception for blank id`() {
        assertFailsWith<IllegalArgumentException> {
            EmailId("   ")
        }
    }

    @Test
    fun `should implement value equality`() {
        val id1 = EmailId("123")
        val id2 = EmailId("123")
        val id3 = EmailId("456")

        assertEquals(id1, id2)
        assertNotEquals(id1, id3)
    }
}