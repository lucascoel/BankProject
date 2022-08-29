package com.example.JayaBank.Model

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.ZonedDateTime

@Document
class Transferencia(
    val id: String? = null,
    val nomeDeOrigem: String,
    val cpfDeOrigem: String,
    val nomeDoDestinatario: String,
    val cpfDestinatario: String,
    val valorDaTrasferencia: BigDecimal,
    val dataDeTransacao: ZonedDateTime
) {
}