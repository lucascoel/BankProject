package com.example.jayaBank.dtos

import com.example.jayaBank.models.Coin
import java.math.BigDecimal

data class AccountResponseDTO(
    val name: String,
    val document: String,
    val balance: BigDecimal,
    val coin: Set<Coin>
)