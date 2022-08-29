package com.example.JayaBank.Service

import com.example.JayaBank.Commons.UserAuth
import com.example.JayaBank.Exception.ContaException
import com.example.JayaBank.Model.Conta
import com.example.JayaBank.Model.Rules
import com.example.JayaBank.Repository.ContaRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClienteService(
    val contaRepository: ContaRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
): UserAuth {

    fun criarConta(conta: Conta): Conta {
        val getConta: Optional<Conta> = contaRepository.findBycpf(conta.cpf)
        val contaPraSalvar = conta.copy(
            Rule = setOf(Rules.USER),
            senha = bCryptPasswordEncoder.encode(conta.senha)
        )
        return if (getConta.isEmpty) {
            contaRepository.save(contaPraSalvar)
        }else{
            throw ContaException("Conta ja existente", HttpStatus.BAD_REQUEST)
        }
    }

    fun atualizarConta(conta: Conta): Conta {
        val getConta: Optional<Conta> = contaRepository.findBycpf(conta.cpf)

        return if (getConta.isEmpty) {
            throw ContaException("Conta não existente", HttpStatus.NOT_FOUND)
        }else{
            contaRepository.save(conta.copy(id = getConta.get().id))
        }
    }

    fun buscarPorCpf(): Optional<Conta> {
        val getConta: Optional<Conta> = contaRepository.findById(userAuthenticated)

        return if (getConta.isEmpty) {
            throw ContaException("Conta não existente", HttpStatus.NOT_FOUND)
        }else{
            getConta
        }
    }
}