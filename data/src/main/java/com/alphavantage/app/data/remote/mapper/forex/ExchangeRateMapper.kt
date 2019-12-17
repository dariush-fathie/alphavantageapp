package com.alphavantage.app.data.remote.mapper.forex

import com.alphavantage.app.data.remote.response.forex.RealtimeExchangeRateResponse
import com.alphavantage.app.domain.model.forex.ExchangeRate
import com.alphavantage.app.domain.util.DateUtils

class ExchangeRateMapper {

    companion object {

        fun mapExchangeRate(response: RealtimeExchangeRateResponse): ExchangeRate {
            val lastDate = DateUtils.parseStringToDate(
                response.exchangeRateResponse.lastRefreshed,
                response.exchangeRateResponse.timeZone
            )
            return ExchangeRate(
                response.exchangeRateResponse.exchangeRate.toDouble(),
                response.exchangeRateResponse.bidPrice.toDouble(),
                response.exchangeRateResponse.askPrice.toDouble(),
                lastDate
            )
        }
    }
}