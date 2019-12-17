package com.alphavantage.app.data.open

import com.alphavantage.app.data.remote.mapper.open.CurrencyMapper
import com.alphavantage.app.domain.model.general.Currency
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrenciesMapperTest {

    lateinit var jsonElement: JsonElement

    @Before
    fun setUp() {
        val parser = JsonParser()
        jsonElement = parser.parse(json)
    }

    @Test
    fun testMapper() {
        assertEquals(list, CurrencyMapper.MapCurrencies(jsonElement))
    }

    private val list = listOf(
        Currency("AED", "United Arab Emirates Dirham"),
        Currency("AFN", "Afghan Afghani"),
        Currency("ALL", "Albanian Lek"),
        Currency("AMD", "Armenian Dram")
    )

    private val json =
        """
            {
              "AED": "United Arab Emirates Dirham",
              "AFN": "Afghan Afghani",
              "ALL": "Albanian Lek",
              "AMD": "Armenian Dram"
            }
        """.trimIndent()
}