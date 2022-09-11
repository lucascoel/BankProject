package com.example.jayaBank.models

import java.math.BigDecimal

data class Account(
    val id: String? = null,
    val name: String,
    val document: String,
    val password: String,
    val balance: BigDecimal,
    val Rule: Set<Rules> = setOf()
) {
}