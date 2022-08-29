package com.example.JayaBank.Controller

import com.example.JayaBank.Model.Conta
import com.example.JayaBank.Service.ClienteService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
class ClienteController(
    val clienteService: ClienteService,
) {

    @PostMapping("/criar")
    fun criarOuAtualizarConta(@RequestBody conta: Conta) = clienteService.criarConta(conta)

    @PutMapping("/editarconta")
    fun editarConta(@RequestBody conta: Conta): Conta = clienteService.atualizarConta(conta)

    @GetMapping("/buscarporcpf")
    fun buscarContasPorCpf(): Optional<Conta> = clienteService.buscarPorCpf()
}