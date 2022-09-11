package com.example.jayaBank.dtos

import java.math.BigDecimal

data class TransferDTO(
    val recipientDocument: String,
    val value: BigDecimal
) {
}