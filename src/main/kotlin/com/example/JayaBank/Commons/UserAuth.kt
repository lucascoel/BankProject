package com.example.JayaBank.Commons

import org.springframework.security.core.context.SecurityContextHolder

interface UserAuth {
    val userAuthenticated: String
        get() = SecurityContextHolder.getContext().authentication.name
}