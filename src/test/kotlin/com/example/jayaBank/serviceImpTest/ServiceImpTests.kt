package com.example.jayaBank.serviceImpTest

import com.example.jayaBank.JayaBankApplication
import com.example.jayaBank.dtos.CreateAccountDTO
import com.example.jayaBank.exceptions.AccountException
import com.example.jayaBank.models.Account
import com.example.jayaBank.repositories.AccountRepository
import com.example.jayaBank.services.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceImpTests {

    @Autowired
    private lateinit var userService: UserService
    @MockkBean
    private lateinit var accountRepository: AccountRepository

    @Test
    fun `test de criar conta para retornar conta criada com sucesso status 200`(){

        val request = CreateAccountDTO(name = "Lucas", document = "191", password = "123", coin = "BRL")
        val response = Account(id = "FOO",name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBydocument(Any() as String) } returns null
        every { accountRepository.save(Any() as Account) } returns response

        val exec = userService.createUserAccount(request)

        assertEquals(response, exec)

    }

    @Test
    fun `test de criar conta para retornar conta ja existente 400`(){

        val request = CreateAccountDTO(name = "Lucas", document = "191", password = "123", coin = "BRL")
        val response = Account(id = "FOO",name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBydocument(Any() as String) } returns response

        assertThrows<AccountException> { userService.createUserAccount(request) }

    }

    @Test
    fun `test de atualizar conta para retornar conta criada com sucesso status 200`(){

        val request = Account(name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)
        val response = Account(id = "FOO",name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBydocument(Any() as String) } returns request
        every { accountRepository.save(Any() as Account) } returns response

        val exec = userService.updateUserAccount(request)

        assertEquals(response, exec)

    }

    @Test
    fun `test de atualizar conta para retornar conta não existente 400`(){

        val request = Account(name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBydocument(Any() as String) } returns null

        assertThrows<AccountException> { userService.updateUserAccount(request) }

    }

}

