package com.example.JayaBank.Dto

import java.math.BigDecimal

data class TransferenciaDto(
    val cpfDestinatario: String,
    val valor: BigDecimal
) {
}