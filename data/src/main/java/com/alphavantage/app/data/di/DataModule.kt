package com.alphavantage.app.data.di

import com.alphavantage.app.data.di.submodule.cryptoRepositoryModule
import com.alphavantage.app.data.di.submodule.generalRepositoryModule
import com.alphavantage.app.data.di.submodule.openApiRepositoryModule

val dataModules = listOf(networkModule, cryptoRepositoryModule, generalRepositoryModule, openApiRepositoryModule)