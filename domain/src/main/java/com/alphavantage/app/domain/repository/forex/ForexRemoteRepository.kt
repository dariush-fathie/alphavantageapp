package com.alphavantage.app.domain.repository.forex

import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.forex.ExchangeRate

interface ForexRemoteRepository {

    suspend fun getExchangeRate(
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Result<ExchangeRate>
}