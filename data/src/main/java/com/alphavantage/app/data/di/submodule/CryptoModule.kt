package com.alphavantage.app.data.di.submodule

import com.alphavantage.app.data.remote.api.CryptoService
import com.alphavantage.app.data.remote.implementation.CryptoRemoteRepositoryImplementation
import com.alphavantage.app.domain.repository.crypto.CryptoRemoteRepository
import org.koin.dsl.module
import retrofit2.Retrofit

fun provideCryptoService(retrofit: Retrofit): CryptoService =
    retrofit.create(CryptoService::class.java)

fun provideCryptoRemoteRepository(service: CryptoService): CryptoRemoteRepository =
    CryptoRemoteRepositoryImplementation(service)

val cryptoRepositoryModule = module {
    factory { provideCryptoService(get()) }
    factory { provideCryptoRemoteRepository(get()) }
}