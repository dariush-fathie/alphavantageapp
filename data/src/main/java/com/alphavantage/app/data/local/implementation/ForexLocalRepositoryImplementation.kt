package com.alphavantage.app.data.local.implementation

import com.alphavantage.app.data.local.ObjectBox
import com.alphavantage.app.data.local.`object`.forex.*
import com.alphavantage.app.data.local.mapper.forex.ExchangeRateMapper
import com.alphavantage.app.data.local.mapper.forex.ForexSeriesMapper
import com.alphavantage.app.domain.model.forex.ExchangeRate
import com.alphavantage.app.domain.model.forex.ForexSeries
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
            val entity =
                ExchangeRateMapper.mapExchangeRate(newExchangeRate, fromCurrency, toCurrency)
            exchangeRateBox.put(entity)
        } else {
            val entity = ExchangeRateEntity(
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

    override fun getExchangeRate(
        currency1: Currency,
        currency2: Currency
    ): ExchangeRate? {
        val exchangeRateBox: Box<ExchangeRateEntity> = ObjectBox.boxStore.boxFor()
        val exchangeRateEntity =
            exchangeRateBox.query().equal(ExchangeRateEntity_.currencyFrom, currency1.code)
                .and()
                .equal(ExchangeRateEntity_.currencyTo, currency1.code).build().findFirst()
        return if (exchangeRateEntity == null)
            null
        else
            ExchangeRateMapper.mapExchangeRate(exchangeRateEntity)
    }

    override suspend fun saveDailySeries(
        series: ForexSeries,
        fromCurrency: Currency,
        toCurrency: Currency
    ) {
        val forexBox: Box<ForexSeriesEntity> = ObjectBox.boxStore.boxFor()
        val oldForex = forexBox.query().equal(ForexSeriesEntity_.currencyFrom, fromCurrency.code).and().equal(ForexSeriesEntity_.currencyTo, toCurrency.code).build().findFirst()
        if (oldForex == null) {
            val entity = ForexSeriesMapper.mapSeries(series, fromCurrency, toCurrency)
            forexBox.put(entity)
        } else {
            oldForex.currencyFrom = fromCurrency.code
            oldForex.currencyTo = toCurrency.code
            oldForex.lastRefreshed = series.lastRefreshed

            val forexItemBox: Box<ForexSeriesItemEntity> = ObjectBox.boxStore.boxFor()
            forexItemBox.query().equal(ForexSeriesItemEntity_.seriesId, oldForex.id)
                .build().remove()
            series.items.forEach {
                oldForex.items.add(
                    ForexSeriesItemEntity(
                        date = it.date,
                        high = it.high,
                        low = it.low,
                        open = it.open,
                        close = it.close
                    )
                )
            }
            forexBox.put(oldForex)
        }
    }

    override fun getDailySeries(fromCurrency: Currency, toCurrency: Currency): ForexSeries? {
        val forexBox: Box<ForexSeriesEntity> = ObjectBox.boxStore.boxFor()
        val entity = forexBox.query().equal(ForexSeriesEntity_.currencyFrom, fromCurrency.code)
            .and()
            .equal(ForexSeriesEntity_.currencyTo, toCurrency.code).build().findFirst()
        return if (entity == null)
            null
        else
            ForexSeriesMapper.mapSeries(entity)
    }
}