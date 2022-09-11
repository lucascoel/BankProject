package com.example.jayaBank.service

import com.example.jayaBank.exception.AuthenticationException
import com.example.jayaBank.repository.AccountRepository
import com.example.jayaBank.security.UserCustomDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val accountRepository: AccountRepository
): UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val account = accountRepository.findById(id)
            .orElseThrow{ AuthenticationException("Falha no login", HttpStatus.UNAUTHORIZED) }
        return UserCustomDetails(account)
    }
}