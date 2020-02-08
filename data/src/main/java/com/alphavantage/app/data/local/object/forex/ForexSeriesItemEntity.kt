package com.alphavantage.app.data.local.`object`.forex

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb
import io.objectbox.relation.ToOne
import java.util.*

@Entity
data class ForexSeriesItemEntity (
    @Id
    var id: Long = 0,
    @NameInDb("date")
    var date: Date,
    @NameInDb("open")
    var open: Double,
    @NameInDb("high")
    var high: Double,
    @NameInDb("low")
    var low: Double,
    @NameInDb("close")
    var close: Double
) {
    lateinit var series: ToOne<ForexSeriesEntity>
}