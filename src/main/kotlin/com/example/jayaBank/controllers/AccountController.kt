package com.example.jayaBank.controllers

import com.example.jayaBank.dtos.TransferExtractDTO
import com.example.jayaBank.dtos.TransferDTO
import com.example.jayaBank.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import javax.validation.Valid

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService
) {

    @PutMapping("/deposit/{balance}")
    fun depositBalance(@PathVariable balance: BigDecimal) = accountService.depositBalanceInAccontUser(balance)

    @PutMapping("/withdraw/{balance}")
    fun withdrawBalance(@PathVariable balance: BigDecimal) = accountService.withdrawBalanceInAccontUser(balance)

    @GetMapping("/checkBalance")
    fun checkBalance() = accountService.checkBalanceInAccountUser()

    @PostMapping("/transfer")
    fun transferBalance(@RequestBody transferDTO: TransferDTO): TransferExtractDTO {
        return accountService.transferBalanceBetweenAccounts(transferDTO)
    }
}