package com.example.JayaBank.Service

import com.example.JayaBank.Commons.UserAuth
import com.example.JayaBank.Dto.ComprovanteDeOperacaoDTO
import com.example.JayaBank.Dto.ComprovanteDeTransferenciaDTO
import com.example.JayaBank.Dto.TransferenciaDto
import com.example.JayaBank.Exception.ContaException
import com.example.JayaBank.Model.Conta
import com.example.JayaBank.Model.Transferencia
import com.example.JayaBank.Repository.ContaRepository
import com.example.JayaBank.Repository.TransferenciaRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@Service
class ContaService(
    val contaRepository: ContaRepository,
    val transferenciaRepository: TransferenciaRepository
) : UserAuth {

    fun adicionarSaldoNaConta(saldoParaAdicionar: BigDecimal): ComprovanteDeOperacaoDTO {
        val contaDoCliente = contaRepository.findById(userAuthenticated)
        val contaComSaldoAtualizado = adicionarSaldoNaContaDoCliente(contaDoCliente.get(), saldoParaAdicionar)
        contaRepository.save(contaComSaldoAtualizado)
        return ComprovanteDeOperacaoDTO(
            saldoAterior = contaDoCliente.get().saldo,
            saldoAtual = contaComSaldoAtualizado.saldo
        )
    }

    fun retirarSaldoNaConta(saldoParaRetirar: BigDecimal): ComprovanteDeOperacaoDTO {
        val contaDoCliente = contaRepository.findById(userAuthenticated)
        val validacaoDeSaldo = validarSaldoConta(contaDoCliente.get(), saldoParaRetirar)
        contaRepository.save(validacaoDeSaldo)
        return ComprovanteDeOperacaoDTO(
            saldoAterior = contaDoCliente.get().saldo,
            saldoAtual = validacaoDeSaldo.saldo
        )
    }

    fun consultarSaldoDoCliente(): String {
        val contaDoCliente = contaRepository.findById(userAuthenticated)
        return "Seu saldo é: ${contaDoCliente.get().saldo}"
    }

    fun transferenciaEntreContas(transferenciaDto: TransferenciaDto): ComprovanteDeTransferenciaDTO {
        try {
            val contaDeOrigem = contaRepository.findById(userAuthenticated)
            val contaDeDestino = buscarPorCpf(transferenciaDto.cpfDestinatario)

            val contaDeOrigemAposTrasferencia = validarSaldoConta(contaDeOrigem.get(), transferenciaDto.valor)
            val contaDeDestinoAposTrasferencia = adicionarSaldoNaContaDoCliente(contaDeDestino, transferenciaDto.valor)

            return salvandoDadosDeTransferencia(
                contaDeOrigemAposTrasferencia,
                contaDeDestinoAposTrasferencia,
                transferenciaDto.valor
            )

        } catch (e: Exception) {
            throw ContaException("Falha ao realizar a transferencia", HttpStatus.BAD_REQUEST)
        }
    }

    private fun validarSaldoConta(conta: Conta, saldoParaRetirar: BigDecimal): Conta {
        return if (conta.saldo >= saldoParaRetirar) {
            val novoSaldo = conta.saldo.minus(saldoParaRetirar)
            conta.copy(saldo = novoSaldo)
        } else {
            throw ContaException("Saldo Insuficiente", HttpStatus.BAD_REQUEST)
        }
    }

    private fun adicionarSaldoNaContaDoCliente(conta: Conta, saldoParaAdicionar: BigDecimal): Conta {
        try {
            val saldoNovo = conta.saldo.plus(saldoParaAdicionar)
            return conta.copy(saldo = saldoNovo)
        } catch (e: Exception) {
            throw ContaException("Erro ao adicionar o saldo", HttpStatus.NOT_ACCEPTABLE)
        }
    }

    private fun buscarPorCpf(cpf: String): Conta {
        val getConta: Optional<Conta> = contaRepository.findBycpf(cpf)

        return if (getConta.isEmpty) {
            throw ContaException("Conta não existente", HttpStatus.NOT_FOUND)
        } else {
            getConta.get()
        }
    }

    private fun salvandoDadosDeTransferencia(
        contaDeOrigemAposTrasferencia: Conta,
        contaDeDestinoAposTrasferencia: Conta,
        valor: BigDecimal
    ): ComprovanteDeTransferenciaDTO {
        val horarioDaTransferencia = ZonedDateTime.now()

        contaRepository.save(contaDeOrigemAposTrasferencia)
        contaRepository.save(contaDeDestinoAposTrasferencia)
        val objetoTransferencia = Transferencia(
            cpfDeOrigem = contaDeOrigemAposTrasferencia.cpf,
            nomeDeOrigem = contaDeOrigemAposTrasferencia.nome,
            cpfDestinatario = contaDeDestinoAposTrasferencia.cpf,
            nomeDoDestinatario = contaDeDestinoAposTrasferencia.nome,
            valorDaTrasferencia = valor,
            dataDeTransacao = horarioDaTransferencia
        )

        transferenciaRepository.save(objetoTransferencia)

        return ComprovanteDeTransferenciaDTO(
            valorDaTrasferencia = valor,
            saldoAterior = contaDeOrigemAposTrasferencia.saldo,
            saldoAtual = contaDeOrigemAposTrasferencia.saldo,
            nomeDoDestinatario = contaDeDestinoAposTrasferencia.nome,
            cpfDestinatario = contaDeDestinoAposTrasferencia.cpf,
            dataDeTransacao = horarioDaTransferencia,
        )
    }

}