package com.example.JayaBank.Repository

import com.example.JayaBank.Model.Conta
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContaRepository: MongoRepository<Conta, String>{

    fun findBycpf(cpf: String): Optional<Conta>
    override fun findById(id: String): Optional<Conta>

}