package com.example.jayaBank.controller

import com.example.jayaBank.dto.CreateAccountDTO
import com.example.jayaBank.model.Account
import com.example.jayaBank.service.UserService
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
    fun searchAccount(): Account? = userService.searchUserAccount()
}