package com.example.jayaBank.repository

import com.example.jayaBank.model.Account
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: MongoRepository<Account, String>{
    fun findBydocument(document: String): Account?

}