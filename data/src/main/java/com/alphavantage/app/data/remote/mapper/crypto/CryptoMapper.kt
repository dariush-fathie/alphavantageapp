package com.alphavantage.app.data.remote.mapper.crypto

import com.alphavantage.app.domain.model.crypto.CryptoSeries
import com.alphavantage.app.domain.model.crypto.CryptoSeriesItem
import com.alphavantage.app.domain.util.DateUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class CryptoMapper {

    companion object {

        fun mapSeries(response: JsonElement): CryptoSeries {
            val meta: JsonObject = response.asJsonObject.getAsJsonObject("Meta Data")
            val lastDate = DateUtils.parseStringToDate(
                meta.get("6. Last Refreshed").asString,
                meta.get("7. Time Zone").asString
            )
            val series: JsonObject =
                response.asJsonObject.getAsJsonObject("Time Series (Digital Currency Monthly)")
            val data = series.entrySet()
                .map {
                    val date = DateUtils.parseStringToDate(it.key, null, "yyyy-MM-dd")
                    val entries = it.value.asJsonObject.entrySet()
                    val iterator = entries.iterator()
                    val openMarketValue = iterator.next().value.asString.toDouble()
                    val openUsdValue = iterator.next().value.asString.toDouble()
                    val highMarketValue = iterator.next().value.asString.toDouble()
                    val highUsdValue = iterator.next().value.asString.toDouble()
                    val lowMarketValue = iterator.next().value.asString.toDouble()
                    val lowUsdValue = iterator.next().value.asString.toDouble()
                    val closeMarketValue = iterator.next().value.asString.toDouble()
                    val closeUsdValue = iterator.next().value.asString.toDouble()
                    val volume = iterator.next().value.asString.toDouble()
                    val marketCap = iterator.next().value.asString.toDouble()
                    CryptoSeriesItem(
                        date = date,
                        openMarket = openMarketValue,
                        openUsd = openUsdValue,
                        highMarket = highMarketValue,
                        highUsd = highUsdValue,
                        lowMarket = lowMarketValue,
                        lowUsd = lowUsdValue,
                        closeMarket = closeMarketValue,
                        closeUsd = closeUsdValue,
                        volume = volume,
                        usdMarketCap = marketCap
                    )
                }
            return CryptoSeries(
                lastRefreshed = lastDate,
                items = data
            )
        }
    }
}