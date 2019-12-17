package com.alphavantage.app.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutinesTestRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher() {

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    fun pause() {
        dispatcher.pauseDispatcher()
    }

    fun resume() {
        dispatcher.resumeDispatcher()
    }
}

@ExperimentalCoroutinesApi
fun CoroutinesTestRule.runBlockingTest(block: suspend () -> Unit) =
    this.dispatcher.runBlockingTest {
        block()
    }

@ExperimentalCoroutinesApi
fun CoroutinesTestRule.pause() = this.pause()

@ExperimentalCoroutinesApi
fun CoroutinesTestRule.resume() = this.resume()