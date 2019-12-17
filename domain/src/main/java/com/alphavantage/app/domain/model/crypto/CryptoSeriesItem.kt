package com.alphavantage.app.domain.model.crypto

import java.util.*

data class CryptoSeriesItem(
    val date: Date,
    val openMarketValue: Double,
    val openUsdValue: Double,
    val highMarketValue: Double,
    val highUsdValue: Double,
    val lowMarketValue: Double,
    val lowUsdValue: Double,
    val closeMarketValue: Double,
    val closeUsdValue: Double,
    val volume: Double,
    val usdMarketCap: Double
)