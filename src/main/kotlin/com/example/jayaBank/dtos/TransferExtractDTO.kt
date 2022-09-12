package com.example.jayaBank.dtos

import java.math.BigDecimal
import java.time.ZonedDateTime

data class TransferExtractDTO(
    val transferValue: BigDecimal,
    val conversionRate: BigDecimal,
    val previousBalance: BigDecimal,
    val currentBalance: BigDecimal,
    val recipient: String,
    val recipientDocument: String,
    val recipientCoin: String,
    val originCoin: String,
    val dateOfTransaction: ZonedDateTime
) {
}