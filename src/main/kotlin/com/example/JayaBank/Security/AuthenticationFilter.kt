package com.example.JayaBank.Security

import com.example.JayaBank.Exception.AuthenticationException
import com.example.JayaBank.Model.Login
import com.example.JayaBank.Repository.ContaRepository
import com.example.JayaBank.Util.JwtUtil
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
    private val contaRepository: ContaRepository,
    private val jwtUtil: JwtUtil
): UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val loginRequest = jacksonObjectMapper().readValue(request.inputStream, Login::class.java)
            val conta = contaRepository.findBycpf(loginRequest.cpf).get().id
            val authToken = UsernamePasswordAuthenticationToken(conta, loginRequest.senha)
            return authenticationManager.authenticate(authToken)
        }catch (e: Exception){
            throw AuthenticationException("Falha no login", HttpStatus.UNAUTHORIZED)
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
