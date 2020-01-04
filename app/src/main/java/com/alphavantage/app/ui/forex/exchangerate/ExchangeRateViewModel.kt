package com.alphavantage.app.ui.forex.exchangerate

import androidx.lifecycle.*
import com.alphavantage.app.domain.usecase.forex.CalculateExchangeRate
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.widget.Event

class ExchangeRateViewModel(
    private val calculateExchangeRate: CalculateExchangeRate,
    private val selectCurrency: SelectCurrency
) : ViewModel() {

    // Variables
    private var fromCurrencyState = selectCurrency.fromCurrency
    val fromCurrency: LiveData<String?> get() = fromCurrencyState.map { it.code }

    private var toCurrencyState = selectCurrency.toCurrency
    val toCurrency: LiveData<String?> get() = toCurrencyState.map { it.code }

    var input = MutableLiveData<String>()

    private var outputState = calculateExchangeRate.data
    val output: LiveData<Double?> get() = outputState.map { it.data }

    // Events
    private var toFromCurrenciesEventState = MutableLiveData<Event<Int>>()
    val toFromCurrenciesEvent: LiveData<Event<Int>> get() = toFromCurrenciesEventState

    private var toToCurrenciesEventState = MutableLiveData<Event<Int>>()
    val toToCurrenciesEvent: LiveData<Event<Int>> get() = toToCurrenciesEventState

    fun calculate() {
        calculateExchangeRate.execute(
            viewModelScope,
            fromCurrencyState.value,
            toCurrencyState.value,
            input.value!!.toDoubleOrNull()
        )
    }

    fun setGoToFromCurrenciesAction(destination: Int) {
        selectCurrency.state = SelectCurrency.SelectState.FROM
        toFromCurrenciesEventState.value = Event(destination)
    }

    fun setGoToToCurrenciesAction(destination: Int) {
        selectCurrency.state = SelectCurrency.SelectState.TO
        toToCurrenciesEventState.value = Event(destination)
    }
}
