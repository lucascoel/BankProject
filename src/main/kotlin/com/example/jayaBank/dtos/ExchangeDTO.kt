package com.example.jayaBank.dtos

import java.math.BigDecimal
import java.sql.Date

data class ExchangeDTO(
    val success: String,
    val timestamp: String,
    val base: String,
    val date: Date,
    val rates: Map<String,BigDecimal>
) {
}