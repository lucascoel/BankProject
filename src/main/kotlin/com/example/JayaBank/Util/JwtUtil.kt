package com.example.JayaBank.Util

import com.example.JayaBank.Exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private val expiration: Int? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun jwtGenerate(id: String): String {
        return Jwts.builder()
            .setSubject(id)
            .setExpiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(SignatureAlgorithm.HS512, secret!!.toByteArray())
            .compact()
    }

    fun isValidToken(token: String): Boolean {
        val claims = getClaims(token)
        if (claims.expiration == null || claims.subject == null || Date().after(claims.expiration)) {
            return false
        }
        return true
    }

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parser().setSigningKey(secret!!.toByteArray()).parseClaimsJws(token).body
        }catch (e: Exception){
            throw AuthenticationException("Token não valido", HttpStatus.UNAUTHORIZED)
        }

    }

    fun getSubject(token: String): String {
        return getClaims(token).subject
    }

}