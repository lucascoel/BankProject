package com.example.jayaBank.dtos

import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

data class ExchangeDTO(
    val success: String,
    val timestamp: Timestamp,
    val base: String,
    val date: Date,
    val rates: Map<String,BigDecimal>
) {
}