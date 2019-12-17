package com.alphavantage.app.ui.forex.exchangerate

sealed class ExchangeRateEvent {

    object OpenFromCurrency : ExchangeRateEvent()
    object OpenToCurrency : ExchangeRateEvent()
    object Calculate : ExchangeRateEvent()
}