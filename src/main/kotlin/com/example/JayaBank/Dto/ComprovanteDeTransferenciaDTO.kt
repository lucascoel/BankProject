package com.example.JayaBank.Dto

import java.math.BigDecimal
import java.time.ZonedDateTime

class ComprovanteDeTransferenciaDTO(
    val valorDaTrasferencia: BigDecimal,
    val saldoAterior: BigDecimal,
    val saldoAtual: BigDecimal,
    val nomeDoDestinatario: String,
    val cpfDestinatario: String,
    val dataDeTransacao: ZonedDateTime
) {
}