package com.example.jayaBank.repositories

import com.example.jayaBank.models.Transfer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransferRepository: MongoRepository<Transfer, String>