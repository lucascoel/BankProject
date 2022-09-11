package com.example.jayaBank.dto

import java.math.BigDecimal

data class OperationExtractDTO(
    val previousBalance: BigDecimal,
    val currentBalance: BigDecimal
)