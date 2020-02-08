package com.alphavantage.app.domain.usecase.forex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.forex.ForexSeries
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.repository.forex.ForexLocalRepository
import com.alphavantage.app.domain.repository.forex.ForexRemoteRepository
import com.alphavantage.app.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineScope

class GetForexSeries(
    private val remote: ForexRemoteRepository,
    private val local: ForexLocalRepository
) : UseCase() {

    private var dataState = MutableLiveData<Result<ForexSeries>>()
    val data: LiveData<Result<ForexSeries>> get() = dataState

    fun executeAndSync(
        scope: CoroutineScope,
        fromCurrency: Currency?,
        toCurrency: Currency?
    ) {
        if (fromCurrency == null) {
            dataState.postValue(Result.error("Kurs asal kosong"))
            return
        }

        if (toCurrency == null) {
            dataState.postValue(Result.error("Kurs tujuan kosong"))
            return
        }

        retrieveNetworkAndSync(scope, {
            when (it.status) {
                Result.Status.LOADING -> dataState.postValue(Result.loading())
                Result.Status.SUCCESS -> dataState.postValue(Result.success(it.data!!))
                Result.Status.ERROR -> dataState.postValue(Result.error(it.message!!))
            }
        }, {
            local.getDailySeries(fromCurrency, toCurrency)
        }, {
            remote.getDailySeries(fromCurrency, toCurrency)
        }, {
            local.saveDailySeries(it, fromCurrency, toCurrency)
        })
    }

    fun execute(
        scope: CoroutineScope,
        fromCurrency: Currency?,
        toCurrency: Currency?
    ) {
        if (fromCurrency == null) {
            dataState.postValue(Result.error("Kurs asal kosong"))
            return
        }

        if (toCurrency == null) {
            dataState.postValue(Result.error("Kurs tujuan kosong"))
            return
        }

        retrieveNetwork(scope, {
            when (it.status) {
                Result.Status.LOADING -> dataState.postValue(Result.loading())
                Result.Status.SUCCESS -> dataState.postValue(Result.success(it.data!!))
                Result.Status.ERROR -> dataState.postValue(Result.error(it.message!!))
            }
        }, {
            remote.getDailySeries(fromCurrency, toCurrency)
        })
    }
}