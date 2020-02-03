package com.alphavantage.app.domain.model.forex

import java.util.*

data class ForexSeries (
    val lastRefreshed: Date,
    val items: List<ForexSeriesItem>
)