package com.example.jayaBank.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class AuthenticationException(mensage: String, httpStatus: HttpStatus): ResponseStatusException(httpStatus, mensage) {
}