package com.example.jayaBank.repository

import com.example.jayaBank.model.Transfer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransferRepository: MongoRepository<Transfer, String>