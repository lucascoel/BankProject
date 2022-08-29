package com.example.JayaBank.Service

import com.example.JayaBank.Exception.AuthenticationException
import com.example.JayaBank.Repository.ContaRepository
import com.example.JayaBank.Security.UserCustomDetails
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val contaRepository: ContaRepository
): UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val conta = contaRepository.findById(id)
            .orElseThrow{ AuthenticationException("Falha no login", HttpStatus.UNAUTHORIZED) }
        return UserCustomDetails(conta)
    }
}