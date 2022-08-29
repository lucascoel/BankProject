package com.example.JayaBank.Dto

import java.math.BigDecimal

data class ComprovanteDeOperacaoDTO(
    val saldoAterior: BigDecimal,
    val saldoAtual: BigDecimal
)