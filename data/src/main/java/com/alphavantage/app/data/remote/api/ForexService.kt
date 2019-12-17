package com.alphavantage.app.data.remote.api

import com.alphavantage.app.data.remote.response.forex.RealtimeExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForexService {

    @GET("query?function=CURRENCY_EXCHANGE_RATE")
    suspend fun getExchangeRate(
        @Query("from_currency") fromCurrency: String,
        @Query("to_currency") toCurrency: String
    ): Response<RealtimeExchangeRateResponse>
}