package com.example.jayaBank.services

import com.example.jayaBank.dtos.OperationExtractDTO
import com.example.jayaBank.exceptions.NotBalanceException
import com.example.jayaBank.mocks.AccountMocks
import com.example.jayaBank.repositories.AccountRepository
import com.example.jayaBank.utils.ExchangeUtil
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class AccountServiceTest() {

    @Autowired
    private lateinit var accountService: AccountService

    @MockkBean
    private lateinit var exchangeUtil: ExchangeUtil

    @MockkBean
    private lateinit var accountRepository: AccountRepository

    val context: SecurityContext = SecurityContextHolder.getContext()

    @BeforeEach
    fun teste() {
        mockkObject(context)
    }

    @Test
    fun `when deposit balance in account, return succes`() {
        val mock = AccountMocks.accountCreated()
        val addBalance = BigDecimal.valueOf(100)
        val response = AccountMocks.accountCreatedWithBalance(addBalance)
        val extract = OperationExtractDTO(
            previousBalance = mock.balance,
            currentBalance = response.balance
        )

        every { accountRepository.findByUserId(any()) } returns mock
        every { context.authentication.name } returns "123"
        every { accountRepository.save(any()) } returns response

        val exec = accountService.depositBalanceInAccontUser(addBalance)

        assertEquals(extract, exec)
    }

    @Test
    fun `when withdraw balance in account, return success`() {
        val mock = AccountMocks.accountCreatedWithBalance(BigDecimal.valueOf(500.00))
        val withdrawValue = BigDecimal.valueOf(100)
        val response = AccountMocks.accountCreatedWithBalance(BigDecimal.valueOf(400.00))
        val extract = OperationExtractDTO(
            previousBalance = mock.balance,
            currentBalance = response.balance
        )

        every { accountRepository.findByUserId(any()) } returns mock
        every { context.authentication.name } returns "123"
        every { accountRepository.save(any()) } returns response

        val exec = accountService.withdrawBalanceInAccontUser(withdrawValue)

        assertEquals(extract, exec)
    }

    @Test
    fun `should withdraw balance in account, when not balance in account, return NotBalanceException`() {
        val mock = AccountMocks.accountCreated()
        val withdrawValue = BigDecimal.valueOf(100)

        every { accountRepository.findByUserId(any()) } returns mock
        every { context.authentication.name } returns "123"

        assertThrows<NotBalanceException> { accountService.withdrawBalanceInAccontUser(withdrawValue) }
    }

    @Test
    fun `when seen balance in account, return balance in account user`() {
        val mock = AccountMocks.accountCreated()

        every { accountRepository.findByUserId(any()) } returns mock
        every { context.authentication.name } returns "123"

        val exec = accountService.checkBalanceInAccountUser()

        assertNotNull(exec)
    }

    @Test
    fun `when transfer balance in account, return success`() {
        val exchange = AccountMocks.exchangeMock()
        val mock = AccountMocks.accountCreated()
        val mockWithBalance = AccountMocks.accountCreatedWithBalance(BigDecimal.valueOf(500.00))
        val transferMock = AccountMocks.transferDtoMock()
        val time = ZonedDateTime.now()

        every { exchangeUtil.client() } returns exchange
        every { accountRepository.findByUserId(any()) } returns mockWithBalance
        every { context.authentication.name } returns "123"
        every { accountRepository.findBydocument(transferMock.recipientDocument) } returns mock
        every { accountRepository.save(any()) } returns mock

        mockkStatic(ZonedDateTime::class)
        every { ZonedDateTime.now() } returns time

        val exec = accountService.transferBalanceBetweenAccounts(transferMock)

        assertEquals("Success Transfer", exec)

    }
}