package com.example.jayaBank.utils

import com.example.jayaBank.dtos.ExchangeDTO
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class ExchangeUtil() {

    fun client(): ExchangeDTO? {
        val client = WebClient
            .builder()
            .baseUrl("https://api.apilayer.com/exchangerates_data/latest?")
            .build()

        return client.method(HttpMethod.GET)
            .uri("&symbols=EUR,BRL,JPY,USD")
            .header("apikey", "d43Bo2nECDNID3WG3quQBDezWOdII3QD")
            .retrieve()
            .bodyToMono<ExchangeDTO>()
            .block()
    }
}