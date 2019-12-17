package com.alphavantage.app.domain.model.crypto

import java.util.*

data class CryptoSeries(
    val lastRefreshed: Date,
    val items: List<CryptoSeriesItem>
)