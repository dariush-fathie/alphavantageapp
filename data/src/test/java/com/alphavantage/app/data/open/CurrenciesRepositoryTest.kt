package com.alphavantage.app.data.open

import com.alphavantage.app.data.networkModule
import com.alphavantage.app.data.remote.api.OpenApiService
import com.alphavantage.app.data.remote.implementation.OpenApiRepositoryImplementation
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.repository.open.OpenApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.Retrofit

class CurrenciesRepositoryTest : KoinTest {

    private val retrofit by inject<Retrofit>()
    private lateinit var repository : OpenApiRepository
    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    @Before
    fun setUp() {
        startKoin {
            modules(networkModule)
        }

        val service = retrofit.create(OpenApiService::class.java)
        repository = OpenApiRepositoryImplementation(service)

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun testGetCurrencies() {
        val res = runBlocking {
            repository.getAllCurrencies()
        }

        assertEquals(Result.Status.SUCCESS, res.status)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()

        stopKoin()
    }
}