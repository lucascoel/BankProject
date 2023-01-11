package com.example.jayaBank.controllers

import com.example.jayaBank.exceptions.AccountException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(AccountException::class)
    fun accountExceptionHandle(e: AccountException) =
        ResponseEntity.status(e.status).body(mapOf("message" to e.message))

}