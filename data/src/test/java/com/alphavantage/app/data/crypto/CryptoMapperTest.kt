package com.alphavantage.app.data.crypto

import com.alphavantage.app.data.remote.mapper.crypto.CryptoMapper
import com.alphavantage.app.domain.model.crypto.CryptoSeries
import com.alphavantage.app.domain.model.crypto.CryptoSeriesItem
import com.alphavantage.app.domain.util.DateUtils
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CryptoMapperTest {

    private lateinit var data: CryptoSeries
    private lateinit var element: JsonElement

    @Before
    fun setUp() {
        data = CryptoSeries(
            lastRefreshed = DateUtils.parseStringToDate("2019-11-20 00:00:00", "UTC"),
            items = listOf(
                CryptoSeriesItem(
                    date = DateUtils.parseStringToDate("2019-11-20", null, "yyyy-MM-dd"),
                    openMarket = 64289.49655200,
                    openUsd = 9140.86000000,
                    highMarket = 66911.61417600,
                    highUsd = 9513.68000000,
                    lowMarket = 56286.69960000,
                    lowUsd = 8003.00000000,
                    closeMarket = 57258.05485200,
                    closeUsd = 8141.11000000,
                    volume = 722441.84657300,
                    usdMarketCap = 722441.84657300
                ),
                CryptoSeriesItem(
                    date = DateUtils.parseStringToDate("2019-10-31", null, "yyyy-MM-dd"),
                    openMarket = 58305.01700400,
                    openUsd = 8289.97000000,
                    highMarket = 72934.28400000,
                    highUsd = 10370.00000000,
                    lowMarket = 51342.36000000,
                    lowUsd = 7300.00000000,
                    closeMarket = 64289.42622000,
                    closeUsd = 9140.85000000,
                    volume = 1446755.85472000,
                    usdMarketCap = 1446755.85472000
                )
            )
        )

        val parser = JsonParser()
        element = parser.parse(json)
    }

    @Test
    fun mapTest() {
        assertEquals(data, CryptoMapper.mapSeries(element))
    }

    private val json = """
        {
            "Meta Data": {
                "1. Information": "Monthly Prices and Volumes for Digital Currency",
                "2. Digital Currency Code": "BTC",
                "3. Digital Currency Name": "Bitcoin",
                "4. Market Code": "CNY",
                "5. Market Name": "Chinese Yuan",
                "6. Last Refreshed": "2019-11-20 00:00:00",
                "7. Time Zone": "UTC"
            },
            "Time Series (Digital Currency Monthly)": {
                "2019-11-20": {
                    "1a. open (CNY)": "64289.49655200",
                    "1b. open (USD)": "9140.86000000",
                    "2a. high (CNY)": "66911.61417600",
                    "2b. high (USD)": "9513.68000000",
                    "3a. low (CNY)": "56286.69960000",
                    "3b. low (USD)": "8003.00000000",
                    "4a. close (CNY)": "57258.05485200",
                    "4b. close (USD)": "8141.11000000",
                    "5. volume": "722441.84657300",
                    "6. market cap (USD)": "722441.84657300"
                },
                "2019-10-31": {
                    "1a. open (CNY)": "58305.01700400",
                    "1b. open (USD)": "8289.97000000",
                    "2a. high (CNY)": "72934.28400000",
                    "2b. high (USD)": "10370.00000000",
                    "3a. low (CNY)": "51342.36000000",
                    "3b. low (USD)": "7300.00000000",
                    "4a. close (CNY)": "64289.42622000",
                    "4b. close (USD)": "9140.85000000",
                    "5. volume": "1446755.85472000",
                    "6. market cap (USD)": "1446755.85472000"
                }
            }
        }
    """.trimIndent()
}