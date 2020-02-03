package com.alphavantage.app.data.local.implementation

import com.alphavantage.app.data.local.ObjectBox
import com.alphavantage.app.data.local.`object`.forex.ExchangeRateEntity
import com.alphavantage.app.data.local.`object`.forex.ExchangeRateEntity_
import com.alphavantage.app.data.local.mapper.general.ExchangeRateMapper
import com.alphavantage.app.domain.model.forex.ExchangeRate
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.repository.forex.ForexLocalRepository
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class ForexLocalRepositoryImplementation : ForexLocalRepository {

    override suspend fun saveExchangeRate(
        newExchangeRate: ExchangeRate,
        fromCurrency: Currency,
        toCurrency: Currency,
        oldExchangeRateId: Long?
    ) {
        val exchangeRateBox: Box<ExchangeRateEntity> = ObjectBox.boxStore.boxFor()
        if (oldExchangeRateId == null) {
            var entity = ExchangeRateMapper.mapExchangeRate(newExchangeRate, fromCurrency, toCurrency)
            exchangeRateBox.put(entity)
        } else {
            var entity = ExchangeRateEntity(
                oldExchangeRateId,
                fromCurrency.code,
                toCurrency.code,
                newExchangeRate.rate,
                newExchangeRate.bidPrice,
                newExchangeRate.askPrice,
                newExchangeRate.lastRefreshedDate
            )
            exchangeRateBox.put(entity)
        }
    }

    override suspend fun getExchangeRate(
        currency1: Currency,
        currency2: Currency
    ): ExchangeRate? {
        val exchangeRateBox: Box<ExchangeRateEntity> = ObjectBox.boxStore.boxFor()
        val exchangeRateEntity =
            exchangeRateBox.query().equal(ExchangeRateEntity_.currencyFrom, currency1.code)
                .equal(ExchangeRateEntity_.currencyTo, currency1.code).build().findFirst()
        return if (exchangeRateEntity == null)
            null
        else
            ExchangeRateMapper.mapExchangeRate(exchangeRateEntity)
    }
}