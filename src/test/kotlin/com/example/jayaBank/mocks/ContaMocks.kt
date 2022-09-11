package com.example.jayaBank.mocks

import com.example.jayaBank.models.Account
import java.math.BigDecimal

class ContaMocks {
    companion object{
        fun approvedInstance(): Account{
            val account = Account(
                id = "FOO",
                name = "Lucas",
                document = "191",
                password = "123",
                balance = BigDecimal.ZERO
            )
            return account
        }


    }
}