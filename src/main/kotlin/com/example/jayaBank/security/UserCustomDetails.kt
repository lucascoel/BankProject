package com.example.jayaBank.security

import com.example.jayaBank.models.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    val account: Account
): UserDetails {
    val id = account.id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = account.rule.map { SimpleGrantedAuthority(it.name) }.toMutableList()

    override fun getPassword(): String = account.password

    override fun getUsername(): String = account.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}