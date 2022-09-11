package com.example.jayaBank.dtos

import java.math.BigDecimal

data class OperationExtractDTO(
    val previousBalance: BigDecimal,
    val currentBalance: BigDecimal
)