package com.example.jayaBank.controller

import com.example.jayaBank.dto.TransferExtractDTO
import com.example.jayaBank.dto.TransferDTO
import com.example.jayaBank.service.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/account")
class AccountController(
    val accountService: AccountService
) {

    @PutMapping("/deposit/{balance}")
    fun depositBalance(@PathVariable balance: BigDecimal) = accountService.depositBalanceInAccontUser(balance)

    @PutMapping("/withdraw/{balance}")
    fun withdrawBalance(@PathVariable balance: BigDecimal) = accountService.withdrawBalanceInAccontUser(balance)

    @GetMapping("/checkBalance")
    fun checkBalance() = accountService.checkBalanceInAccountUser()

    @PostMapping("/transfer")
    fun transferenciaDeSaldo(@RequestBody transferDTO: TransferDTO): TransferExtractDTO {
        return accountService.transferBalanceBetweenAccounts(transferDTO)
    }
}