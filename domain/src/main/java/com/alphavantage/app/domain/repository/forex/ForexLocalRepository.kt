package com.alphavantage.app.domain.repository.forex

import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.model.forex.ExchangeRate

interface ForexLocalRepository {

    suspend fun saveExchangeRate(
        newExchangeRate: ExchangeRate,
        fromCurrency: Currency,
        toCurrency: Currency,
        oldExchangeRateId: Long?
    )

    suspend fun getExchangeRate(
        currency1: Currency,
        currency2: Currency): ExchangeRate?
}