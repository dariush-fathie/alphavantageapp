package com.alphavantage.app.ui.common

import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventConsumedContent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.consume()?.run(onEventConsumedContent)
    }
}