package com.alphavantage.app.data.remote.mapper.forex

import com.alphavantage.app.domain.model.forex.ForexSeries
import com.alphavantage.app.domain.model.forex.ForexSeriesItem
import com.alphavantage.app.domain.util.DateUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class ForexSeriesMapper {

    companion object {

        fun mapSeries(response: JsonElement): ForexSeries {
            val meta: JsonObject = response.asJsonObject.getAsJsonObject("Meta Data")
            val lastDate = DateUtils.parseStringToDate(
                meta.get("5. Last Refreshed").asString,
                meta.get("6. Time Zone").asString
            )
            val series: JsonObject =
                response.asJsonObject.getAsJsonObject("Time Series FX (Daily)")
            val data = series.entrySet()
                .map {
                    val date = DateUtils.parseStringToDate(it.key, null, "yyyy-MM-dd")
                    val entries = it.value.asJsonObject.entrySet()
                    val iterator = entries.iterator()
                    val openValue = iterator.next().value.asString.toDouble()
                    val highValue = iterator.next().value.asString.toDouble()
                    val lowValue = iterator.next().value.asString.toDouble()
                    val closeValue = iterator.next().value.asString.toDouble()
                    ForexSeriesItem(
                        date = date,
                        open = openValue,
                        high = highValue,
                        low = lowValue,
                        close = closeValue
                    )
                }
            return ForexSeries(
                lastRefreshed = lastDate,
                items = data
            )
        }
    }
}