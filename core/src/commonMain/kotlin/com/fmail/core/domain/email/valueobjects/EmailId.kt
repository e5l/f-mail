package com.fmail.core.domain.email.valueobjects

data class EmailId(val value: String) {
    init {
        require(value.isNotBlank()) { "Email ID cannot be empty or blank" }
    }
}