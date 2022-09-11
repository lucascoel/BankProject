package com.example.jayaBank.models

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.ZonedDateTime

@Document
data class Transfer(
    val id: String? = null,
    val name: String,
    val document: String,
    val recipientName: String,
    val recipientDocument: String,
    val transferValue: BigDecimal,
    val dateOfTransaction: ZonedDateTime
) {
}