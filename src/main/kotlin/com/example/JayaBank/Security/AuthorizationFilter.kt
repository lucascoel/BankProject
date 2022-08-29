package com.example.JayaBank.Security

import com.example.JayaBank.Exception.AuthenticationException
import com.example.JayaBank.Service.UserDetailsCustomService
import com.example.JayaBank.Util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val userDetail: UserDetailsCustomService,
    private val jwtUtil: JwtUtil
): BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorization = request.getHeader("Authorization")
        if (authorization != null && authorization.startsWith("Bearer ")){
            val userAuth = getAuthorization(authorization.split(" ")[1])
            SecurityContextHolder.getContext().authentication = userAuth
        }
        chain.doFilter(request, response)
    }

    private fun getAuthorization(token: String): UsernamePasswordAuthenticationToken {
        if (!jwtUtil.isValidToken(token)){
            throw AuthenticationException("Token não valido", HttpStatus.UNAUTHORIZED)
        }
        val subject = jwtUtil.getSubject(token)
        val user = userDetail.loadUserByUsername(subject)
        return UsernamePasswordAuthenticationToken(subject, null, user.authorities)

    }

}