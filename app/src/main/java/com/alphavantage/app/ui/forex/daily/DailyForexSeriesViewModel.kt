package com.alphavantage.app.ui.forex.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.alphavantage.app.domain.usecase.forex.GetForexSeries
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.util.addDays
import com.alphavantage.app.domain.util.addHours
import com.alphavantage.app.domain.util.atStartOfTheDay
import com.alphavantage.app.domain.worker.DailyForexWorker
import java.util.*
import java.util.concurrent.TimeUnit

// TODO run worker from ViewModel
class DailyForexSeriesViewModel(
    private val getForexSeries: GetForexSeries,
    private val selectCurrency: SelectCurrency
) : ViewModel() {

    // Variables
    private var fromCurrencyState = selectCurrency.fromCurrency
    val fromCurrency: LiveData<String?> get() = fromCurrencyState.map { it.code }

    private var toCurrencyState = selectCurrency.toCurrency
    val toCurrency: LiveData<String?> get() = toCurrencyState.map { it.code }

    lateinit var updateWorker: PeriodicWorkRequest

    init {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val date = Date()

        val delay = if (hour < 8) {
            val datePreferred = date.atStartOfTheDay().addHours(8)
            TimeUnit.MILLISECONDS.toMinutes(datePreferred.time - date.time)
        } else {
            val datePreferred = date.atStartOfTheDay().addDays(1).addHours(1)
            TimeUnit.MILLISECONDS.toMinutes(datePreferred.time - date.time)
        }

        updateWorker = PeriodicWorkRequest.Builder(
            DailyForexWorker::class.java,
            24,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .addTag("daily_series_update")
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork("daily_series_update", ExistingPeriodicWorkPolicy.REPLACE, updateWorker)
    }

    fun getSeries() {
        getForexSeries.executeAndSync(
            viewModelScope,
            fromCurrencyState.value,
            toCurrencyState.value
        )
    }

    override fun onCleared() {
        super.onCleared()
        WorkManager.getInstance().cancelUniqueWork("daily_series_update")
    }
}
