package com.example.jayaBank.mocks

import com.example.jayaBank.dtos.*
import com.example.jayaBank.models.Account
import com.example.jayaBank.models.Coin
import com.example.jayaBank.models.Rules
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.ZonedDateTime

class AccountMocks {
    companion object{
        fun createAccontSuccess() = CreateAccountDTO(
            name = "Lucas",
            document = "144",
            password = "123",
            coin = "BRL"
        )

        fun createAccountSuccessResponse() = AccountResponseDTO(
            name = "XPTO",
            document = "144",
            balance = BigDecimal.ZERO,
            coin = setOf(Coin.BRL)
        )

        fun accountCreated() = Account(
            id = "FOO",
            name = "XPTO",
            document = "144",
            password = "123",
            balance = BigDecimal.ZERO,
            rule = setOf(Rules.USER),
            coin = setOf(Coin.BRL)
        )

        fun accountCreatedWithBalance(value: BigDecimal) = Account(
            id = "FOO",
            name = "BAR",
            document = "144",
            password = "123",
            balance = value,
            rule = setOf(Rules.USER),
            coin = setOf(Coin.BRL)
        )

        fun accountForUpdate() = Account(
            id = "FOO",
            name = "Lucas",
            document = "144",
            password = "45678",
            balance = BigDecimal.ZERO,
            rule = setOf(Rules.USER),
            coin = setOf(Coin.BRL)
        )

        fun createAccontFailedCoin() = CreateAccountDTO(
            name = "Lucas",
            document = "191",
            password = "123",
            coin = "FOO"
        )

        fun transferDtoMock() = TransferDTO(
            recipientDocument = "144",
            value = BigDecimal.valueOf(50)
        )

        fun transferWithAccountInTheSameCountry() = TransferExtractDTO(
            transferValue = BigDecimal.valueOf(50.00).setScale(2),
            conversionRate = BigDecimal.valueOf(1.00).setScale(2),
            previousBalance = BigDecimal.valueOf(500),
            currentBalance = BigDecimal.valueOf(450.00).setScale(2),
            recipient = "XPTO",
            recipientDocument = "144",
            recipientCoin = "BRL",
            originCoin = "BRL",
            dateOfTransaction = ZonedDateTime.now()
        )

        fun exchangeMock() = ExchangeDTO(
            success = "true",
            timestamp = "",
            base = "EUR",
            date = Date.valueOf("2022-10-25"),
            rates = mapOf(
                "BRL" to  BigDecimal.valueOf(5),
                "USD" to  BigDecimal.valueOf(2.14),
                "JPY" to  BigDecimal.valueOf(10.88),
                "EUR" to  BigDecimal.valueOf(1))
        )
    }
}