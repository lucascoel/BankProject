package com.example.jayaBank.dtos

data class CreateAccountDTO(
    val name: String,
    val document: String,
    val password: String,
    val coin: String
) {
}