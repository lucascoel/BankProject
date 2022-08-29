package com.example.JayaBank.ServiceImpTest

import com.example.JayaBank.Exception.ContaException
import com.example.JayaBank.Model.Conta
import com.example.JayaBank.Repository.ContaRepository
import com.example.JayaBank.Service.ClienteService
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import java.math.BigDecimal
import java.util.*

@SpringBootTest
class ServiceImpTests(
    val clienteService: ClienteService,
    val contaRepository: ContaRepository
    ) {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `test de criar conta para retornar conta criada com sucesso status 200`(){

        val request = Conta(nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)
        val response = Conta(id = "FOO",nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)

        every { contaRepository.findBycpf(Any() as String) } returns Optional.empty()
        every { contaRepository.save(Any() as Conta) } returns response

        val exec = clienteService.criarConta(request)

        assertEquals(response, exec)

    }

    @Test
    fun `test de criar conta para retornar conta ja existente 400`(){

        val request = Conta(nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)
        val response = Conta(id = "FOO",nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)

        every { contaRepository.findBycpf(Any() as String) } returns Optional.of(response)
        every { contaRepository.save(Any() as Conta) } returns response

        assertThrows<ContaException> { clienteService.criarConta(request) }

    }

    @Test
    fun `test de atualizar conta para retornar conta criada com sucesso status 200`(){

        val request = Conta(nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)
        val response = Conta(id = "FOO",nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)

        every { contaRepository.findBycpf(Any() as String) } returns Optional.of(response)
        every { contaRepository.save(Any() as Conta) } returns response

        val exec = clienteService.atualizarConta(request)

        assertEquals(response, exec)

    }

    @Test
    fun `test de atualizar conta para retornar conta ja existente 400`(){

        val request = Conta(nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)
        val response = Conta(id = "FOO",nome = "Lucas", cpf = "191", senha = "123", saldo = BigDecimal.ZERO)

        every { contaRepository.findBycpf(Any() as String) } returns Optional.empty()
        every { contaRepository.save(Any() as Conta) } returns response

        assertThrows<ContaException> { clienteService.atualizarConta(request) }

    }

}

