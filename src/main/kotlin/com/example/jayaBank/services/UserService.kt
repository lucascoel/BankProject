package com.example.jayaBank.services

import com.example.jayaBank.commons.UserAuth
import com.example.jayaBank.dtos.AccountResponseDTO
import com.example.jayaBank.dtos.CreateAccountDTO
import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.models.Account
import com.example.jayaBank.models.Coin
import com.example.jayaBank.models.Rules
import com.example.jayaBank.repositories.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class UserService(
    private val accountRepository: AccountRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserAuth {
    fun createUserAccount(account: CreateAccountDTO) =
        accountRepository.findBydocument(account.document)
            .takeIf { it?.document.isNullOrBlank() }
            ?.let { Account.createAccount(account, bCryptPasswordEncoder) }
            ?.let { accountRepository.save(it) }
            ?.also{ print("alou") }
            ?: throw AccountException("Account exist", HttpStatus.BAD_REQUEST)


    fun updateUserAccount(account: Account): Account =
        accountRepository.findBydocument(account.document)
            .takeUnless { it?.document == null }
            ?.let { accountRepository.save(account.updateAccount(it.userId!!)) }
            ?: throw AccountException("Account not found", HttpStatus.NOT_FOUND)


    fun searchUserAccount(): AccountResponseDTO =
        accountRepository.findByUserId(userAuthenticated)
            .let { AccountResponseDTO(it.name, it.document, it.balance, it.coin) }


}