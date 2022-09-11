package com.example.jayaBank.serviceImpTest

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

@SpringBootTest
class ServiceImpTests {

    @Autowired
    private lateinit var userService: UserService
    @MockkBean
    private lateinit var accountRepository: AccountRepository

    @Test
    fun `test de criar conta para retornar conta criada com sucesso status 200`(){

        val request = Account(name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)
        val response = Account(id = "FOO",name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBycpf(Any() as String) } returns null
        every { accountRepository.save(Any() as Account) } returns response

        val exec = userService.criarConta(request)

        assertEquals(response, exec)

    }

    @Test
    fun `test de criar conta para retornar conta ja existente 400`(){

        val request = Account(name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBycpf(Any() as String) } returns request

        assertThrows<AccountException> { userService.criarConta(request) }

    }

    @Test
    fun `test de atualizar conta para retornar conta criada com sucesso status 200`(){

        val request = Account(name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)
        val response = Account(id = "FOO",name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBycpf(Any() as String) } returns request
        every { accountRepository.save(Any() as Account) } returns response

        val exec = userService.atualizarConta(request)

        assertEquals(response, exec)

    }

    @Test
    fun `test de atualizar conta para retornar conta não existente 400`(){

        val request = Account(name = "Lucas", document = "191", password = "123", balance = BigDecimal.ZERO)

        every { accountRepository.findBycpf(Any() as String) } returns null

        assertThrows<AccountException> { userService.atualizarConta(request) }

    }

}

