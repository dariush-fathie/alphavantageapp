package com.alphavantage.app.ui.forex.daily

import androidx.lifecycle.*
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.forex.ForexSeries
import com.alphavantage.app.domain.model.forex.ForexSeriesItem
import com.alphavantage.app.domain.usecase.forex.GetForexSeries
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.widget.DefaultDispatcherProvider
import com.alphavantage.app.domain.widget.DispatcherProvider

// TODO run worker from ViewModel
class DailyForexSeriesViewModel(
    private val getForexSeries: GetForexSeries,
    private val selectCurrency: SelectCurrency,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    // Variables
    private val _fromCurrency = selectCurrency.fromCurrency
    val fromCurrencyCode: LiveData<String> get() = _fromCurrency.map { it.code }
    val fromCurrencyName: LiveData<String> get() = _fromCurrency.map { it.name }
    private val _toCurrency = selectCurrency.toCurrency
    val toCurrencyCode: LiveData<String> get() = _toCurrency.map { it.code }
    val toCurrencyName: LiveData<String> get() = _toCurrency.map { it.name }

    val items: LiveData<Result<ForexSeries>> get() = getForexSeries.data
    private var _highlightedItem: MutableLiveData<ForexSeriesItem?> = MutableLiveData(null)
    val openPrice: LiveData<String> get() = _highlightedItem.map { if (it != null) "${toCurrencyCode.value} ${it.open}" else "No item selected" }
    val closePrice: LiveData<String> get() = _highlightedItem.map { if (it != null) "${toCurrencyCode.value} ${it.close}" else "No item selected" }
    val lowPrice: LiveData<String> get() = _highlightedItem.map { if (it != null) "${toCurrencyCode.value} ${it.low}" else "No item selected" }
    val highPrice: LiveData<String> get() = _highlightedItem.map { if (it != null) "${toCurrencyCode.value} ${it.high}" else "No item selected" }

    fun highlightItem(item: ForexSeriesItem) {
        _highlightedItem.postValue(item)
    }

    fun getSeries() {
        getForexSeries.executeAndSync(
            viewModelScope,
            _fromCurrency.value,
            _toCurrency.value,
            dispatcherProvider
        )
    }
}
