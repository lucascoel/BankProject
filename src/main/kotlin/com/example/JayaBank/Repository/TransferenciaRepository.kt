package com.example.JayaBank.Repository

import com.example.JayaBank.Model.Transferencia
import org.springframework.data.mongodb.repository.MongoRepository

interface TransferenciaRepository: MongoRepository<Transferencia, String>