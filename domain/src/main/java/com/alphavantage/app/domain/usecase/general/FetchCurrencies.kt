package com.alphavantage.app.domain.usecase.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.repository.open.OpenApiRepository
import com.alphavantage.app.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineScope

class FetchCurrencies(private val remote: OpenApiRepository) : UseCase() {

    private var dataState: MutableLiveData<Result<List<Currency>>> = MutableLiveData()
    val data: LiveData<Result<List<Currency>>> get() = dataState

    fun execute(scope: CoroutineScope) {
        retrieveNetwork(scope, { dataState.postValue(it) }, { remote.getAllCurrencies() })
    }
}