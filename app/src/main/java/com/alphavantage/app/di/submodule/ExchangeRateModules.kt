package com.alphavantage.app.di.submodule

import com.alphavantage.app.ui.forex.daily.DailyForexSeriesViewModel
import com.alphavantage.app.ui.forex.exchangerate.ExchangeRateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangeRateViewModules = module {
    viewModel { DailyForexSeriesViewModel(get(), get()) }
    viewModel { ExchangeRateViewModel(get(), get()) }
}