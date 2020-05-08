package com.alphavantage.app.forex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.forex.ExchangeRate
import com.alphavantage.app.domain.model.general.Currency
import com.alphavantage.app.domain.repository.forex.ForexRemoteRepository
import com.alphavantage.app.domain.usecase.general.SelectCurrency
import com.alphavantage.app.domain.util.getOrAwaitValue
import com.alphavantage.app.domain.widget.DispatcherProvider
import com.alphavantage.app.rule.CoroutinesTestRule
import com.alphavantage.app.rule.runBlockingTest
import com.alphavantage.app.testModules
import com.alphavantage.app.ui.exchangerate.ExchangeRateViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ExchangeRateViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    private val viewModel: ExchangeRateViewModel by inject()
    private val selectCurrency: SelectCurrency by inject()

    @Mock
    lateinit var fromCurrency: Currency
    @Mock
    lateinit var toCurrency: Currency
    @Mock
    lateinit var result: ExchangeRate

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        startKoin {
            modules(testModules)
        }

        declare { factory { coroutinesRule.testDispatcherProvider } }
    }

    @Test
    fun testCalculate() {
        coroutinesRule.runBlockingTest {
            `when`(fromCurrency.code).thenReturn("USD")
            `when`(toCurrency.code).thenReturn("JPY")

            selectCurrency.fromCurrency.postValue(fromCurrency)
            selectCurrency.toCurrency.postValue(toCurrency)
            viewModel.input.postValue("1")

            viewModel.calculate()

            val value = viewModel.output.getOrAwaitValue()!!
            assertEquals(107.52, value, 0.001)
        }
    }
}