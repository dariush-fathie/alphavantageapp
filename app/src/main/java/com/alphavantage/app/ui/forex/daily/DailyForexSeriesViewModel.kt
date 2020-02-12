package com.alphavantage.app.ui.forex.daily

import android.content.Context
import androidx.lifecycle.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.alphavantage.app.domain.usecase.forex.GetForexSeries
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.util.addDays
import com.alphavantage.app.domain.util.atStartOfTheDay
import com.alphavantage.app.domain.worker.DailyForexWorker
import java.util.*
import java.util.concurrent.TimeUnit

// TODO run worker from ViewModel
class DailyForexSeriesViewModel(
    private val getForexSeries: GetForexSeries,
    selectCurrency: SelectCurrency
) : ViewModel(), LifecycleObserver {

    // Variables
    private var fromCurrencyState = selectCurrency.fromCurrency
    val fromCurrency: LiveData<String?> get() = fromCurrencyState.map { it.code }

    private var toCurrencyState = selectCurrency.toCurrency
    val toCurrency: LiveData<String?> get() = toCurrencyState.map { it.code }

    fun setLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startWork(context: Context) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val date = Date()

        val datePreferred = date.atStartOfTheDay().addDays(1)
        val delay = TimeUnit.MILLISECONDS.toMinutes(datePreferred.time - date.time)

        val updateWorker = PeriodicWorkRequestBuilder<DailyForexWorker>(
            24,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .addTag("daily_series_update")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_series_update",
            ExistingPeriodicWorkPolicy.REPLACE,
            updateWorker
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun endWork(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("daily_series_update")
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
    }
}
