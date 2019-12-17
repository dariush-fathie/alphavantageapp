package com.alphavantage.app.open

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.util.getOrAwaitValue
import com.alphavantage.app.rule.CoroutinesTestRule
import com.alphavantage.app.rule.runBlockingTest
import com.alphavantage.app.testModules
import com.alphavantage.app.ui.currencies.CurrenciesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

@RunWith(JUnit4::class)
class CurrenciesViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    private val viewModel: CurrenciesViewModel by inject()

    private val testLiveData = MutableLiveData<Int>()

    @Before
    fun setUp() {
        startKoin {
            modules(testModules)
        }
    }

    @Test
    fun testGetCurrencies() {
        coroutinesRule.runBlockingTest {
            viewModel.getItems()

            coroutinesRule.pause()
            val results1 = viewModel.items.getOrAwaitValue()
            assertEquals(Result.Status.LOADING, results1.status)

            coroutinesRule.resume()
            val results2 = viewModel.items.getOrAwaitValue()
            assertEquals(Result.Status.SUCCESS, results2.status)
        }
    }

    @Test
    fun testLiveData() {
        coroutinesRule.runBlockingTest {
            someSuspendedLiveData()

            coroutinesRule.pause()
            assertEquals(1, testLiveData.value)

            coroutinesRule.resume()
            assertEquals(2, testLiveData.getOrAwaitValue())
        }
    }

    private suspend fun someSuspendedLiveData() {
        testLiveData.postValue(1)

        delay(5_000)

        testLiveData.postValue(2)
    }
}