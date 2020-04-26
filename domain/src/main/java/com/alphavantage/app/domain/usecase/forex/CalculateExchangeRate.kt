package com.alphavantage.app.domain.usecase.forex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.repository.forex.ForexRemoteRepository
import com.alphavantage.app.domain.usecase.UseCase
import com.alphavantage.app.domain.widget.DefaultDispatcherProvider
import com.alphavantage.app.domain.widget.DispatcherProvider
import kotlinx.coroutines.CoroutineScope

class CalculateExchangeRate(private val remote: ForexRemoteRepository) :
    UseCase() {

    private var dataState = MutableLiveData<Result<Double>>()
    val data: LiveData<Result<Double>> get() = dataState

    fun execute(
        scope: CoroutineScope,
        fromCurrency: Currency?,
        toCurrency: Currency?,
        input: Double?,
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
    ) {
        if (fromCurrency == null) {
            dataState.postValue(Result.error("Kurs asal kosong"))
            return
        }

        if (toCurrency == null) {
            dataState.postValue(Result.error("Kurs tujuan kosong"))
            return
        }

        if (input == null) {
            dataState.postValue(Result.error("Input kosong"))
            return
        }

        retrieveNetwork(scope, {
            when (it.status) {
                Result.Status.SUCCESS -> dataState.postValue(Result.success(it.data!!.rate * input))
                Result.Status.ERROR -> dataState.postValue(Result.error(it.message!!))
                Result.Status.LOADING -> dataState.postValue(Result.loading())
            }
        }, { remote.getExchangeRate(fromCurrency, toCurrency) }, dispatcherProvider)
    }
}