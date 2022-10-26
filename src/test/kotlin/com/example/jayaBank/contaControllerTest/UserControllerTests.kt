package com.example.jayaBank.contaControllerTest

import com.example.jayaBank.mocks.AccountMocks
import com.example.jayaBank.services.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var userService: UserService

    @Test
    fun `should correct call, when payload is correct, returns created`() {
        val test = AccountMocks.createAccontSuccess()
        val requestJson = jacksonObjectMapper().writeValueAsString(test)
        val response = AccountMocks.createAccountSuccessResponse()

        every { userService.createUserAccount(any()) } returns response

        val request = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/user/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andReturn().response

        assertEquals(HttpStatus.CREATED.value(), request.status)
    }

    @Test
    fun `should update account, when payload correct and account exist, return ok`() {
        val account = AccountMocks.accountCreated()
        val requestJson = jacksonObjectMapper().writeValueAsString(account)


        every { userService.updateUserAccount(any()) } returns account

        val request = mockMvc.perform(
            MockMvcRequestBuilders
                .put("/user/updateAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )

        val exec =
            request.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andReturn().response
        assertEquals(HttpStatus.OK.value(), exec.status)
    }

}



