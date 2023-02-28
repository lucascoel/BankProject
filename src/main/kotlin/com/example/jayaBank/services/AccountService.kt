package com.example.jayaBank.services

import com.example.jayaBank.commons.UserAuth
import com.example.jayaBank.dtos.OperationExtractDTO
import com.example.jayaBank.dtos.TransferDTO
import com.example.jayaBank.dtos.TransferExtractDTO
import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.exceptions.NotFoundException
import com.example.jayaBank.models.Account
import com.example.jayaBank.models.Transfer
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
            accountRepository.findByUserId(userAuthenticated)
                    .depositBalanceInAccontUser(valueDeposit)
                    .let { accountRepository.save(it) }
                    .run { toOperationExtract(this, this.balance.minus(valueDeposit)) }

    fun withdrawBalanceInAccontUser(balanceToWithdraw: BigDecimal): OperationExtractDTO =
            accountRepository.findByUserId(userAuthenticated)
                    .withdrawAccountBalance(balanceToWithdraw)
                    .let { accountRepository.save(it) }
                    .run { toOperationExtract(this, this.balance.plus(balanceToWithdraw)) }
                    .also { println("$it success withdraw") }

    fun checkBalanceInAccountUser() =
            accountRepository.findByUserId(userAuthenticated)
                    .let { it.balance }
                    .also { println("${it} checked balance account") }

    fun transferBalanceBetweenAccounts(transferDTO: TransferDTO): TransferExtractDTO {
        val userAuthenticatedAccount = accountRepository.findByUserId(userAuthenticated)
        val recipientAccount = searchDocument(transferDTO.recipientDocument)

        val conversionRate =
                createConversionRate(recipientAccount.coin.joinToString(), userAuthenticatedAccount.coin.joinToString())

        userAuthenticatedAccount.transfer(transferDTO.value.multiply(conversionRate), recipientAccount)

        return savingTransfer(transferDTO, userAuthenticatedAccount, recipientAccount, conversionRate)
    }

    private fun searchDocument(cpf: String) =
            accountRepository.findBydocument(cpf)?.also { println("${it.userId} searched") }
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

    private fun toOperationExtract(account: Account, balance: BigDecimal) =
            OperationExtractDTO(
                    balance,
                    account.balance
            )

    private fun savingTransfer(
            transferDTO: TransferDTO,
            userAuthenticatedAccountUpdated: Account,
            recipientAccountUpdated: Account,
            conversionRate: BigDecimal
    ): TransferExtractDTO {
        val dateOfTransfer = ZonedDateTime.now()

        val objetoTransfer = Transfer(
                name = userAuthenticatedAccountUpdated.name,
                document = userAuthenticatedAccountUpdated.document,
                recipientName = recipientAccountUpdated.name,
                recipientDocument = recipientAccountUpdated.document,
                transferValue = transferDTO.value,
                dateOfTransaction = dateOfTransfer
        )

        transferRepository.save(objetoTransfer)

        return TransferExtractDTO(
                transferValue = transferDTO.value,
                conversionRate = conversionRate,
                previousBalance = transferDTO.value,
                currentBalance = userAuthenticatedAccountUpdated.balance,
                recipient = recipientAccountUpdated.name,
                recipientDocument = recipientAccountUpdated.document,
                recipientCoin = recipientAccountUpdated.coin.joinToString(),
                originCoin = userAuthenticatedAccountUpdated.coin.joinToString(),
                dateOfTransaction = dateOfTransfer
        ).also { println(it) }
    }

}