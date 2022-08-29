package com.example.JayaBank.Mocks

import com.example.JayaBank.Model.Conta
import java.math.BigDecimal

class ContaMocks {
    companion object{
        fun approvedInstance(): Conta{
            val conta = Conta(
                id = "FOO",
                nome = "Lucas",
                cpf = "191",
                senha = "123",
                saldo = BigDecimal.ZERO
            )
            return conta
        }


    }
}