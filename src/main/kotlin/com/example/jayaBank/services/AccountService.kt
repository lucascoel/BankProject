package com.example.jayaBank.services

import com.example.jayaBank.commons.UserAuth
import com.example.jayaBank.dtos.OperationExtractDTO
import com.example.jayaBank.dtos.TransferDTO
import com.example.jayaBank.dtos.TransferExtractDTO
import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.exceptions.NotFoundException
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
    private val accountRepository: AccountRepository,
    private val transferRepository: TransferRepository
) : UserAuth {

    fun depositBalanceInAccontUser(valueDeposit: BigDecimal) =
        accountRepository.findByUserAuthenticated(userAuthenticated)
            .depositBalanceInAccontUser(valueDeposit)
            .let { accountRepository.save(it) }

    fun withdrawBalanceInAccontUser(balanceToWithdraw: BigDecimal): OperationExtractDTO =
        accountRepository.findById(userAuthenticated)
            .get()
            .withdrawAccountBalance(balanceToWithdraw)
            .let { accountRepository.save(it) }
            .run { toOperationExtract(this) }
            .also { println("$it success withdraw") }

    fun checkBalanceInAccountUser() =
        accountRepository.findByUserAuthenticated(userAuthenticated)
            .let { it.balance }
            .also { println("${it} checked balance account") }

    fun transferBalanceBetweenAccounts(transferDTO: TransferDTO): TransferExtractDTO {
        try {

            val userAuthenticatedAccount = accountRepository.findById(userAuthenticated).get()
            val recipientAccount = searchDocument(transferDTO.recipientDocument)

            val conversionRate =
                createConversionRate(recipientAccount.coin.joinToString(), userAuthenticatedAccount.coin.joinToString())
            val conversionValue = transferDTO.value.multiply(conversionRate)

            val userAuthenticatedAccountUpdated =
                userAuthenticatedAccount.withdrawAccountBalance(conversionValue)
            val recipientAccountUpdated = recipientAccount.depositBalanceInAccontUser(conversionValue)

            return savingTransfer(
                userAuthenticatedAccount.balance,
                userAuthenticatedAccountUpdated,
                recipientAccountUpdated,
                conversionValue,
                conversionRate
            )

        } catch (e: AccountException) {
            throw AccountException("Transfer fail", HttpStatus.BAD_REQUEST)
        }
    }

    private fun searchDocument(cpf: String) =
        accountRepository.findBydocument(cpf)?.also { println("${it.id} searched") }
            ?: throw NotFoundException("Account not found", HttpStatus.NOT_FOUND)
                .also { println(it.cause?.message) }


    private fun createConversionRate(recipientCoin: String, userAuthenticatedCoin: String): BigDecimal {
        val exchange = ExchangeUtil().client()
            .also { println("${it?.success} get exchange rate for conversion") }

        return exchange!!.rates[recipientCoin]!!
            .divide(
                exchange!!.rates[userAuthenticatedCoin],
                2,
                RoundingMode.HALF_EVEN
            )
    }

    private fun toOperationExtract(account: Account) =
        OperationExtractDTO(
            BigDecimal.ZERO,
            BigDecimal.ZERO
        )


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