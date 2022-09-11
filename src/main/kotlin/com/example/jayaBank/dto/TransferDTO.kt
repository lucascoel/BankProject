package com.example.jayaBank.dto

import java.math.BigDecimal

data class TransferDTO(
    val recipientDocument: String,
    val value: BigDecimal
) {
}