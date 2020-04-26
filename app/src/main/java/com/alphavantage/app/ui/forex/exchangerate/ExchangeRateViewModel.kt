package com.alphavantage.app.ui.forex.exchangerate

import androidx.lifecycle.*
import com.alphavantage.app.domain.usecase.forex.CalculateExchangeRate
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.widget.DefaultDispatcherProvider
import com.alphavantage.app.domain.widget.DispatcherProvider
import com.alphavantage.app.domain.widget.Event

class ExchangeRateViewModel(
    private val calculateExchangeRate: CalculateExchangeRate,
    private val selectCurrency: SelectCurrency,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    // Variables
    private var _fromCurrency = selectCurrency.fromCurrency
    val fromCurrency: LiveData<String?> get() = _fromCurrency.map { it.code }

    private var _toCurrency = selectCurrency.toCurrency
    val toCurrency: LiveData<String?> get() = _toCurrency.map { it.code }

    var input = MutableLiveData<String>()

    private var _output = calculateExchangeRate.data
    val output: LiveData<Double?> get() = _output.map { it.data }

    // Events
    private var _toFromCurrenciesEvent = MutableLiveData<Event<Int>>()
    val toFromCurrenciesEvent: LiveData<Event<Int>> get() = _toFromCurrenciesEvent

    private var _toToCurrenciesEvent = MutableLiveData<Event<Int>>()
    val toToCurrenciesEvent: LiveData<Event<Int>> get() = _toToCurrenciesEvent

    private var _toDailyEvent = MutableLiveData<Event<Unit>>()
    val toDailyEvent: LiveData<Event<Unit>> get() = _toDailyEvent

    fun calculate() {
        calculateExchangeRate.execute(
            viewModelScope,
            _fromCurrency.value,
            _toCurrency.value,
            input.value!!.toDoubleOrNull(),
            dispatcherProvider
        )
    }

    fun openDaily() {
        _toDailyEvent.value = Event(Unit)
    }

    fun setGoToFromCurrenciesAction(destination: Int) {
        selectCurrency.state = SelectCurrency.SelectState.FROM
        _toFromCurrenciesEvent.value = Event(destination)
    }

    fun setGoToToCurrenciesAction(destination: Int) {
        selectCurrency.state = SelectCurrency.SelectState.TO
        _toToCurrenciesEvent.value = Event(destination)
    }
}
