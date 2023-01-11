package com.example.jayaBank.models

import com.example.jayaBank.dtos.CreateAccountDTO
import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.exceptions.NotBalanceException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.math.BigDecimal

class Account(
    id: String?,
    val name: String,
    val document: String,
    val password: String,
    balance: BigDecimal,
    val rule: Set<Rules> = setOf(),
    val coin: Set<Coin> = setOf()
) {
    var balance = balance
        private set
    var id = id
        private set
    fun withdrawAccountBalance(balanceToWithdraw: BigDecimal) =
        also {
            this.takeUnless { balance >= balanceToWithdraw }
                ?.let { balance -= balanceToWithdraw }
                ?: throw NotBalanceException("insufficient funds", HttpStatus.BAD_REQUEST)
        }

    fun depositBalanceInAccontUser(valueDeposit: BigDecimal) =
        also {
            this.takeUnless { valueDeposit <= BigDecimal.ZERO }
                ?.let { balance += valueDeposit }
                ?: throw AccountException("insufficient funds", HttpStatus.BAD_REQUEST)
        }

    fun transfer(value: BigDecimal, recipientAccount: Account) =
        this.takeUnless { value <= BigDecimal.ZERO }.let {
            this.withdrawAccountBalance(value)
            recipientAccount.depositBalanceInAccontUser(value)
            "Success Transfer"
        }

    fun createAccount(account: CreateAccountDTO, bCryptPasswordEncoder: BCryptPasswordEncoder) = Account(
        id = null,
        name = account.name,
        document = account.document,
        password = bCryptPasswordEncoder.encode(account.password),
        balance = BigDecimal.ZERO,
        rule = setOf(Rules.USER),
        coin = setOf(Coin.valueOf(account.coin))
    )
    fun updateAccount(idUpdate: String) = this.apply { id = idUpdate }

}