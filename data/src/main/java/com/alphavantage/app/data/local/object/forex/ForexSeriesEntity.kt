package com.alphavantage.app.data.local.`object`.forex

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import io.objectbox.relation.ToMany
import java.util.*

@Entity
data class ForexSeriesEntity(
    @Id
    var id: Long = 0,
    @NameInDb("currency_from")
    var currencyFrom: String,
    @NameInDb("currency_to")
    var currencyTo: String,
    @NameInDb("last_refreshed")
    var lastRefreshed: Date
) {
    lateinit var items: ToMany<ForexSeriesItemEntity>
}