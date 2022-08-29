package com.example.JayaBank.Security

import com.example.JayaBank.Model.Conta
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    val conta: Conta
): UserDetails {
    val id = conta.id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = conta.Rule.map { SimpleGrantedAuthority(it.name) }.toMutableList()

    override fun getPassword(): String = conta.senha

    override fun getUsername(): String = conta.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}