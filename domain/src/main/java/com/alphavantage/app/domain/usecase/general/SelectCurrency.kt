package com.alphavantage.app.domain.usecase.general

import androidx.lifecycle.MutableLiveData
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.usecase.UseCase

class SelectCurrency : UseCase() {

    val fromCurrency = MutableLiveData<Currency>()
    val toCurrency = MutableLiveData<Currency>()

    var state: SelectState = SelectState.FROM

    fun setCurrency(currency: Currency) {
        if (state == SelectState.FROM) {
            setFromCurrency(currency)
        } else if (state == SelectState.TO) {
            setToCurrency(currency)
        }
    }

    private fun setFromCurrency(currency: Currency) {
        fromCurrency.postValue(currency)
    }

    private fun setToCurrency(currency: Currency) {
        toCurrency.postValue(currency)
    }

    enum class SelectState {
        FROM,
        TO
    }
}