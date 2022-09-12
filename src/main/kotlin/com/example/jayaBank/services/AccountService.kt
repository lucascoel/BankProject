package com.example.jayaBank.services

import com.example.jayaBank.commons.UserAuth
import com.example.jayaBank.dtos.OperationExtractDTO
import com.example.jayaBank.dtos.TransferDTO
import com.example.jayaBank.dtos.TransferExtractDTO
import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.models.Account
import com.example.jayaBank.repositories.AccountRepository
import com.example.jayaBank.repositories.TransferRepository
import com.example.jayaBank.utils.ExchangeUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZonedDateTime

@Service
class AccountService(
    val accountRepository: AccountRepository,
    val transferRepository: TransferRepository
) : UserAuth {

    fun depositBalanceInAccontUser(valueDeposit: BigDecimal): OperationExtractDTO {
        val userAccount = accountRepository.findById(userAuthenticated).get()
        val userAccountUpdated = userAccount.depositBalanceInAccontUser(userAccount, valueDeposit)
        accountRepository.save(userAccountUpdated)
        return OperationExtractDTO(
            previousBalance = userAccount.balance,
            currentBalance = userAccountUpdated.balance
        ).also { println("${it} success deposit") }
    }

    fun withdrawBalanceInAccontUser(balanceToWithdraw: BigDecimal): OperationExtractDTO {
        val userAccount = accountRepository.findById(userAuthenticated).get()
        val validBalance = userAccount.validAccountBalance(userAccount, balanceToWithdraw)
        accountRepository.save(validBalance)
        return OperationExtractDTO(
            userAccount.balance,
            validBalance.balance
        ).also { println("$it success withdraw") }
    }

    fun checkBalanceInAccountUser(): BigDecimal {
        val userAccount = accountRepository.findById(userAuthenticated)
            .also { println("${it.get().document} checked balance account") }
        return userAccount.get().balance
    }

    fun transferBalanceBetweenAccounts(transferDTO: TransferDTO): TransferExtractDTO {
        try {
            val userAuthenticatedAccount = accountRepository.findById(userAuthenticated).get()
            val recipientAccount = searchDocument(transferDTO.recipientDocument)

            val conversionRate =
                createConversionRate(recipientAccount.coin.joinToString(), userAuthenticatedAccount.coin.joinToString())
            val conversionValue = transferDTO.value.multiply(conversionRate)

            val userAuthenticatedAccountUpdated =
                userAuthenticatedAccount.validAccountBalance(userAuthenticatedAccount, conversionValue)
            val recipientAccountUpdated = recipientAccount.depositBalanceInAccontUser(recipientAccount, conversionValue)

            return savingTransfer(
                userAuthenticatedAccount.balance,
                userAuthenticatedAccountUpdated,
                recipientAccountUpdated,
                conversionValue,
                conversionRate
            )

        } catch (e: Exception) {
            throw AccountException("Transfer fail", HttpStatus.BAD_REQUEST)
        }
    }

    private fun searchDocument(cpf: String): Account {
        val getAccount: Account? = accountRepository.findBydocument(cpf)
            .also { println("${it?.id} searched") }

        return getAccount ?: throw AccountException("Account not found", HttpStatus.NOT_FOUND)
    }

    private fun createConversionRate(recipientCoin: String, userAuthenticatedCoin: String): BigDecimal {
        val exchange = ExchangeUtil().client()
            .also { println("${it} get exchange rate for conversion") }

        return exchange!!.rates.get(recipientCoin)!!
            .divide(
                exchange!!.rates.get(userAuthenticatedCoin),
                6,
                RoundingMode.HALF_EVEN
            )
    }

    private fun savingTransfer(
        previousBalance: BigDecimal,
        userAuthenticatedAccountUpdated: Account,
        recipientAccountUpdated: Account,
        value: BigDecimal,
        conversionRate: BigDecimal
    ): TransferExtractDTO {
        val dateOfTransfer = ZonedDateTime.now()

        accountRepository.save(userAuthenticatedAccountUpdated)
            .also { println("${it.id} saved after transfer") }

        accountRepository.save(recipientAccountUpdated)
            .also { println("${it.id} saved after transfer") }

//        val objetoTransfer = Transfer(
//            id = null,
//            name = userAuthenticatedAccountUpdated.name,
//            document = userAuthenticatedAccountUpdated.document,
//            recipientName = recipientAccountUpdated.name,
//            recipientDocument = recipientAccountUpdated.document,
//            transferValue = value,
//            dateOfTransaction = dateOfTransfer
//        )
//
//        transferRepository.save(objetoTransfer)

        return TransferExtractDTO(
            transferValue = value,
            conversionRate = conversionRate,
            previousBalance = previousBalance,
            currentBalance = userAuthenticatedAccountUpdated.balance,
            recipient = recipientAccountUpdated.name,
            recipientDocument = recipientAccountUpdated.document,
            recipientCoin = recipientAccountUpdated.coin.joinToString(),
            originCoin = userAuthenticatedAccountUpdated.coin.joinToString(),
            dateOfTransaction = dateOfTransfer
        ).also { println(it) }
    }

}