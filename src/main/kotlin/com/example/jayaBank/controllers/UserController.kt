package com.example.jayaBank.controllers

import com.example.jayaBank.dtos.CreateAccountDTO
import com.example.jayaBank.models.Account
import com.example.jayaBank.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService
) {

    @PostMapping("/createAccount")
    fun createAccount(@RequestBody account: CreateAccountDTO) = userService.createUserAccount(account)

    @PutMapping("/updateAccount")
    fun updateAccount(@RequestBody account: Account): Account = userService.updateUserAccount(account)

    @GetMapping("/searchAccount")
    fun searchAccount() = userService.searchUserAccount()
}