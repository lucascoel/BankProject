package com.example.jayaBank.repositories

import com.example.jayaBank.models.Account
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: MongoRepository<Account, String>{
    fun findBydocument(document: String): Account?

}
