package com.example.jayaBank.models

import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.exceptions.NotBalanceException
import org.springframework.http.HttpStatus
import java.math.BigDecimal

data class Account(
    val id: String? = null,
    val name: String,
    val document: String,
    val password: String,
    val balance: BigDecimal,
    val rule: Set<Rules> = setOf(),
    val coin: Set<Coin> = setOf()
) {

    fun withdrawAccountBalance(account: Account, balanceToWithdraw: BigDecimal): Account {
        return if (account.balance >= balanceToWithdraw) {
            val newBalance = account.balance.minus(balanceToWithdraw)
            account.copy(balance = newBalance)
        } else {
            throw NotBalanceException("insufficient funds", HttpStatus.BAD_REQUEST)
        }
    }

    fun depositBalanceInAccontUser(account: Account, valueDeposit: BigDecimal): Account {
        try {
            val newBalance = account.balance.plus(valueDeposit)
            return account.copy(balance = newBalance)
        } catch (e: Exception) {
            throw AccountException("Deposit fail", HttpStatus.BAD_REQUEST)
        }
    }
}