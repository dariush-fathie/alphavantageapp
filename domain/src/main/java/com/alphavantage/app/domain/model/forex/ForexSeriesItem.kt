package com.alphavantage.app.domain.model.forex

import java.util.*

data class ForexSeriesItem (
    val date: Date,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)