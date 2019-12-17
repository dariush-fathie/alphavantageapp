package com.alphavantage.app.data.di.submodule

import com.alphavantage.app.data.local.implementation.ForexLocalRepositoryImplementation
import com.alphavantage.app.data.remote.api.ForexService
import com.alphavantage.app.data.remote.implementation.ForexRemoteRepositoryImplementation
import com.alphavantage.app.domain.repository.forex.ForexLocalRepository
import com.alphavantage.app.domain.repository.forex.ForexRemoteRepository
import org.koin.dsl.module
import retrofit2.Retrofit

fun provideGeneralService(retrofit: Retrofit): ForexService =
    retrofit.create(ForexService::class.java)

fun provideGeneralRemoteRepository(service: ForexService): ForexRemoteRepository =
    ForexRemoteRepositoryImplementation(service)

fun provideGeneralLocalRepository(): ForexLocalRepository = ForexLocalRepositoryImplementation()

val generalRepositoryModule = module {
    factory { provideGeneralService(get()) }
    factory { provideGeneralRemoteRepository(get()) }
    factory { provideGeneralLocalRepository() }
}