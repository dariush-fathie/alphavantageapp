package com.alphavantage.app.ui.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.usecase.general.FetchCurrencies
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.widget.Event
import timber.log.Timber

class CurrenciesViewModel(
    private val fetchCurrencies: FetchCurrencies,
    private val selectCurrency: SelectCurrency
) : ViewModel() {
    // TODO: Implement the ViewModel

    val items: LiveData<Event<Result<List<Currency>>>> get() = fetchCurrencies.data

    private var _toPreviousEvent = MutableLiveData<Event<Unit>>()
    val toPreviousEvent: LiveData<Event<Unit>> get() = _toPreviousEvent

    fun itemClick(item: Currency) {
        // TODO back to ViewModel
        Timber.i(item.name)
        selectCurrency.setCurrency(item)
        backToPrevious()
    }

    fun getItems() {
        fetchCurrencies.execute(viewModelScope)
    }

    private fun backToPrevious() {
        _toPreviousEvent.value = Event(Unit)
    }
}
