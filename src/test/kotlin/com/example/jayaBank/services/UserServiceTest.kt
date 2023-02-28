package com.example.jayaBank.services

import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.mocks.AccountMocks
import com.example.jayaBank.repositories.AccountRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
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
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class UserServiceTest() {

    @Autowired
    private lateinit var userService: UserService

    @MockK
    private lateinit var userAuth: SecurityContext

    @MockkBean
    private lateinit var accountRepository: AccountRepository

    val context: SecurityContext = SecurityContextHolder.getContext()
    @BeforeEach
    fun teste(){
        mockkObject(context)
    }

    @Test
    fun `when create account with payload correct, expected status 201`() {

        val request = AccountMocks.createAccontSuccess()
        val response = AccountMocks.accountCreated()

        every { accountRepository.findBydocument(any()) } returns null
        every { accountRepository.save(any()) } returns response

        val exec = userService.createUserAccount(request)

        assertNotNull(exec)
    }

    @Test
    fun `when create account with payload not correct, expected status 400`() {

        val request = AccountMocks.createAccontSuccess()
        val response = AccountMocks.accountCreated()

        every { accountRepository.findBydocument(any()) } returns response

        assertThrows<AccountException> { userService.createUserAccount(request) }

    }

    @Test
    fun `when update account expected success`() {

        val request = AccountMocks.accountCreated()
        val response = AccountMocks.accountForUpdate()

        every { accountRepository.findBydocument(any()) } returns request
        every { accountRepository.save(any()) } returns response

        val exec = userService.updateUserAccount(request)

        assertEquals(response, exec)

    }

    @Test
    fun `when update account expected account not found`() {

        val request = AccountMocks.accountCreated()

        every { accountRepository.findBydocument(any()) } returns null

        assertThrows<AccountException> { userService.updateUserAccount(request) }
    }

    @Test
    fun `should search account, when id valid, return success`() {

        val mock = AccountMocks.accountCreated()
        val response = AccountMocks.createAccountSuccessResponse()

        every { accountRepository.findByUserId(any()) } returns mock
        every { context.authentication.name } returns "123"


        val exec = userService.searchUserAccount()

        assertEquals(response, exec)
    }
}
