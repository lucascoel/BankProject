package com.example.JayaBank.Controller

import com.example.JayaBank.Dto.ComprovanteDeTransferenciaDTO
import com.example.JayaBank.Dto.TransferenciaDto
import com.example.JayaBank.Service.ContaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class ContaController(
    val contaService: ContaService
) {

    @PutMapping("/adicionarsaldo/{saldo}")
    fun adicionarSaldo(@PathVariable saldo: BigDecimal) = contaService.adicionarSaldoNaConta(saldo)

    @PutMapping("/retirarsaldo/{saldo}")
    fun retirarSaldo(@PathVariable saldo: BigDecimal) = contaService.retirarSaldoNaConta(saldo)

    @GetMapping("/consultarsaldo")
    fun consultarSaldo() = contaService.consultarSaldoDoCliente()

    @PostMapping("/transferir")
    fun transferenciaDeSaldo(@RequestBody transferenciaDto: TransferenciaDto): ComprovanteDeTransferenciaDTO {
        return contaService.transferenciaEntreContas(transferenciaDto)
    }
}