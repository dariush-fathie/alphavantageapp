package com.alphavantage.app.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.usecase.forex.GetForexSeries
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject

class DailyForexWorker(private val context: Context, private val workerParams: WorkerParameters) :
    CoroutineWorker(
        context,
        workerParams
    ), KoinComponent {

    private val getForexSeries: GetForexSeries by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val fromCurrencyCode = inputData.getString("from_currency_code")
        val fromCurrencyName = inputData.getString("from_currency_name")
        val fromCurrency = Currency(fromCurrencyCode!!, fromCurrencyName!!)
        val toCurrencyCode = inputData.getString("to_currency_code")
        val toCurrencyName = inputData.getString("to_currency_name")
        val toCurrency = Currency(toCurrencyCode!!, toCurrencyName!!)
        getForexSeries.executeAndSync(this, fromCurrency, toCurrency)

        Result.success()
    }
}