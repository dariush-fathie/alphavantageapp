package com.alphavantage.app.data.local.mapper.forex

import com.alphavantage.app.data.local.`object`.forex.ForexSeriesEntity
import com.alphavantage.app.data.local.`object`.forex.ForexSeriesItemEntity
import com.alphavantage.app.domain.model.forex.ForexSeries
import com.alphavantage.app.domain.model.forex.ForexSeriesItem
import com.alphavantage.app.domain.model.general.Currency

class ForexSeriesMapper {

    companion object {

        fun mapSeries(entity: ForexSeriesEntity): ForexSeries {
            return ForexSeries(
                entity.lastRefreshed,
                entity.items.map {
                    ForexSeriesItem(
                        it.date,
                        it.open,
                        it.high,
                        it.low,
                        it.close
                    )
                }
            )
        }

        fun mapSeries(model: ForexSeries, fromCurrency: Currency, toCurrency: Currency): ForexSeriesEntity {
            val entity = ForexSeriesEntity(
                lastRefreshed = model.lastRefreshed,
                currencyFrom = fromCurrency.code,
                currencyTo = toCurrency.code
            )
            model.items.forEach {
                entity.items.add(
                    ForexSeriesItemEntity(
                        date = it.date,
                        high = it.high,
                        low = it.low,
                        open = it.open,
                        close = it.close
                    )
                )
            }

            return entity
        }
    }
}