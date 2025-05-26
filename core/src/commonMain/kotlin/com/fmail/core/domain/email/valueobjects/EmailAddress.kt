package com.fmail.core.domain.email.valueobjects

data class EmailAddress(val value: String) {
    init {
        require(value.isNotBlank()) { "Email address cannot be empty" }
        require(isValidEmail(value)) { "Invalid email format: $value" }
    }

    val domain: String
        get() = value.substringAfter('@')

    val localPart: String
        get() = value.substringBefore('@')

    companion object {
        private fun isValidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
            return emailRegex.matches(email)
        }
    }
}