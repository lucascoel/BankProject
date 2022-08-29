package com.example.JayaBank.Model

import java.math.BigDecimal

data class Conta(
    val id: String? = null,
    val nome: String,
    val cpf: String,
    val senha: String,
    val saldo: BigDecimal,
    val Rule: Set<Rules> = setOf()
) {
}