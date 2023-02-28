package com.example.jayaBank.security

import com.example.jayaBank.exceptions.AuthenticationException
import com.example.jayaBank.models.Login
import com.example.jayaBank.repositories.AccountRepository
import com.example.jayaBank.utils.JwtUtil
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val accountRepository: AccountRepository,
    private val jwtUtil: JwtUtil
): UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val loginRequest = jacksonObjectMapper().readValue(request.inputStream, Login::class.java)
            val account = accountRepository.findBydocument(loginRequest.document)?.userId
            val authToken = UsernamePasswordAuthenticationToken(account, loginRequest.password)
            return authenticationManager.authenticate(authToken)
        }catch (e: Exception){
            throw AuthenticationException("Not Authorization", HttpStatus.UNAUTHORIZED)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val id = (authResult.principal as UserCustomDetails).id
        val token = jwtUtil.jwtGenerate(id.toString())
        response.addHeader("Authorization", "Bearer $token")
    }

}
