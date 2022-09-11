package com.example.jayaBank.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class AccountException(mensage: String, httpStatus: HttpStatus): ResponseStatusException(httpStatus, mensage) {
}