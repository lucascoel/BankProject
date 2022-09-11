package com.example.jayaBank.dtos

import java.math.BigDecimal
import java.time.ZonedDateTime

class TransferExtractDTO(
    val transferValue: BigDecimal,
    val previousBalance: BigDecimal,
    val currentBalance: BigDecimal,
    val recipient: String,
    val recipientDocument: String,
    val dateOfTransaction: ZonedDateTime
) {
}