package com.alphavantage.app.domain.usecase.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.repository.open.OpenApiRepository
import com.alphavantage.app.domain.usecase.UseCase
import com.alphavantage.app.domain.util.postEventValue
import com.alphavantage.app.domain.widget.Event
import kotlinx.coroutines.CoroutineScope

class FetchCurrencies(private val remote: OpenApiRepository) : UseCase() {

    private var dataState: MutableLiveData<Event<Result<List<Currency>>>> = MutableLiveData()
    val data: LiveData<Event<Result<List<Currency>>>> get() = dataState

    fun execute(scope: CoroutineScope) {
        retrieveNetwork(scope, { dataState.postEventValue(it) }, { remote.getAllCurrencies() })
    }
}