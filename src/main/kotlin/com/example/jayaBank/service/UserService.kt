package com.example.jayaBank.service

import com.example.jayaBank.commons.UserAuth
import com.example.jayaBank.dto.CreateAccountDTO
import com.example.jayaBank.exception.AccountException
import com.example.jayaBank.model.Account
import com.example.jayaBank.model.Rules
import com.example.jayaBank.repository.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class UserService(
    val accountRepository: AccountRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
): UserAuth {

    fun createUserAccount(account: CreateAccountDTO): Account {
        val getAccount: Account? = accountRepository.findBydocument(account.document)
        val newAccount = Account(
            name = account.name,
            document = account.document,
            balance = BigDecimal.ZERO,
            Rule = setOf(Rules.USER),
            password = bCryptPasswordEncoder.encode(account.password)
        )
        return if (getAccount == null) {
            accountRepository.save(newAccount)
        }else{
            throw AccountException("Account exist", HttpStatus.BAD_REQUEST)
        }
    }

    fun updateUserAccount(account: Account): Account {
        val getAccount: Account? = accountRepository.findBydocument(account.document)

        return if (getAccount == null) {
            throw AccountException("Account not found", HttpStatus.NOT_FOUND)
        }else{
            accountRepository.save(account.copy(id = getAccount.id))
        }
    }

    fun searchUserAccount(): Account {
        val getAccount: Optional<Account> = accountRepository.findById(userAuthenticated)

        return if (getAccount.isEmpty) {
            throw AccountException("Account not found", HttpStatus.NOT_FOUND)
        }else{
            getAccount.get()
        }
    }
}