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

@Service
class UserService(
    val accountRepository: AccountRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserAuth {

    fun createUserAccount(account: CreateAccountDTO): AccountResponseDTO {
        val getAccount: Account? = accountRepository.findBydocument(account.document)
        val newAccount = Account(
            name = account.name,
            document = account.document,
            balance = BigDecimal.ZERO,
            rule = setOf(Rules.USER),
            password = bCryptPasswordEncoder.encode(account.password),
            coin = setOf(Coin.valueOf(account.coin))
        )

        return if (getAccount == null) {
            accountRepository.save(newAccount)
            AccountResponseDTO(newAccount.name, newAccount.document, newAccount.balance, newAccount.coin)
        } else {
            throw AccountException("Account exist", HttpStatus.BAD_REQUEST)
        }
    }

    fun updateUserAccount(account: Account): Account {
        val getAccount: Account? = accountRepository.findBydocument(account.document)

        return if (getAccount == null) {
            throw AccountException("Account not found", HttpStatus.NOT_FOUND)
        } else {
            accountRepository.save(account.copy(id = getAccount.id))
        }
    }

    fun searchUserAccount(): AccountResponseDTO {
        val getAccount: Account = accountRepository.findById(userAuthenticated).get()
        return AccountResponseDTO(getAccount.name, getAccount.document, getAccount.balance, getAccount.coin)
    }
}