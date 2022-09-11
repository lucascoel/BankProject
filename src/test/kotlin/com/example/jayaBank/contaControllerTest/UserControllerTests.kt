package com.example.jayaBank.contaControllerTest

import com.example.jayaBank.model.Account
import com.example.jayaBank.service.UserService
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userService: UserService

    @Test
    fun `teste pra dar certo`() {
        val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        val test = Account(name = "Lucas", password = "123", balance = BigDecimal.ZERO, document = "191")
        val requestJson = mapper.writeValueAsString(test)

        val request = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andReturn().response

        Assertions.assertEquals(HttpStatus.OK.value(), request.status)
    }

}



