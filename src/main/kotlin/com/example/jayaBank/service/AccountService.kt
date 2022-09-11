package com.example.jayaBank.service

import com.example.jayaBank.commons.UserAuth
import com.example.jayaBank.dto.OperationExtractDTO
import com.example.jayaBank.dto.TransferExtractDTO
import com.example.jayaBank.dto.TransferDTO
import com.example.jayaBank.exception.AccountException
import com.example.jayaBank.model.Account
import com.example.jayaBank.model.Transfer
import com.example.jayaBank.repository.AccountRepository
import com.example.jayaBank.repository.TransferRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.ZonedDateTime

@Service
class AccountService(
    val accountRepository: AccountRepository,
    val transferRepository: TransferRepository
) : UserAuth {

    fun depositBalanceInAccontUser(valueDeposit: BigDecimal): OperationExtractDTO {
        val userAccount = accountRepository.findById(userAuthenticated)
        val userAccountUpdated = depositBalanceInAccontUser(userAccount.get(), valueDeposit)
        accountRepository.save(userAccountUpdated)
        return OperationExtractDTO(
            previousBalance = userAccount.get().balance,
            currentBalance = userAccountUpdated.balance
        )
    }

    fun withdrawBalanceInAccontUser(balanceToWithdraw: BigDecimal): OperationExtractDTO {
        val userAccount = accountRepository.findById(userAuthenticated)
        val validacaoDeSaldo = validAccountBalance(userAccount.get(), balanceToWithdraw)
        accountRepository.save(validacaoDeSaldo)
        return OperationExtractDTO(
            userAccount.get().balance,
            validacaoDeSaldo.balance
        )
    }

    fun checkBalanceInAccountUser(): BigDecimal {
        val userAccount = accountRepository.findById(userAuthenticated)
        return userAccount.get().balance
    }

    fun transferBalanceBetweenAccounts(transferDTO: TransferDTO): TransferExtractDTO {
        try {
            val userAuthenticatedAccount = accountRepository.findById(userAuthenticated)
            val recipientAccount = searchDocument(transferDTO.recipientDocument)

            val userAuthenticatedAccountUpdated = validAccountBalance(userAuthenticatedAccount.get(), transferDTO.value)
            val recipientAccountUpdated = depositBalanceInAccontUser(recipientAccount, transferDTO.value)

            return savingTransfer(
                userAuthenticatedAccountUpdated,
                recipientAccountUpdated,
                transferDTO.value
            )

        } catch (e: Exception) {
            throw AccountException("Transfer fail", HttpStatus.BAD_REQUEST)
        }
    }

    private fun validAccountBalance(account: Account, balanceToWithdraw: BigDecimal): Account {
        return if (account.balance >= balanceToWithdraw) {
            val newBalance = account.balance.minus(balanceToWithdraw)
            account.copy(balance = newBalance)
        } else {
            throw AccountException("insufficient funds", HttpStatus.BAD_REQUEST)
        }
    }

    private fun depositBalanceInAccontUser(account: Account, valueDeposit: BigDecimal): Account {
        try {
            val newBalance = account.balance.plus(valueDeposit)
            return account.copy(balance = newBalance)
        } catch (e: Exception) {
            throw AccountException("Deposit fail", HttpStatus.NOT_ACCEPTABLE)
        }
    }

    private fun searchDocument(cpf: String): Account {
        val getAccount: Account? = accountRepository.findBydocument(cpf)

        return getAccount ?: throw AccountException("Account not found", HttpStatus.NOT_FOUND)
    }

    private fun savingTransfer(
        userAuthenticatedAccountUpdated: Account,
        recipientAccountUpdated: Account,
        value: BigDecimal
    ): TransferExtractDTO {
        val dateOfTransfer = ZonedDateTime.now()

        accountRepository.save(userAuthenticatedAccountUpdated)
        accountRepository.save(recipientAccountUpdated)
        val objetoTransfer = Transfer(
            id = null,
            name = userAuthenticatedAccountUpdated.name,
            document = userAuthenticatedAccountUpdated.document,
            recipientName = recipientAccountUpdated.name,
            recipientDocument = recipientAccountUpdated.document,
            transferValue = value,
            dateOfTransaction = dateOfTransfer
        )

        transferRepository.save(objetoTransfer)

        return TransferExtractDTO(
            transferValue = value,
            previousBalance = userAuthenticatedAccountUpdated.balance,
            currentBalance = userAuthenticatedAccountUpdated.balance,
            recipient = recipientAccountUpdated.name,
            recipientDocument = recipientAccountUpdated.document,
            dateOfTransaction = dateOfTransfer
        )
    }

}