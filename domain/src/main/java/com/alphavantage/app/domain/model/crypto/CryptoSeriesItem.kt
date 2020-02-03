package com.alphavantage.app.domain.model.crypto

import java.util.*

data class CryptoSeriesItem(
    val date: Date,
    val openMarket: Double,
    val openUsd: Double,
    val highMarket: Double,
    val highUsd: Double,
    val lowMarket: Double,
    val lowUsd: Double,
    val closeMarket: Double,
    val closeUsd: Double,
    val volume: Double,
    val usdMarketCap: Double
)