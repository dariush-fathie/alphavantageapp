package com.alphavantage.app.domain.usecase

import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.widget.DefaultDispatcherProvider
import com.alphavantage.app.domain.widget.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UseCase {

    protected fun <T> retrieveNetworkAndSync(
        scope: CoroutineScope,
        emitValue: (Result<T>) -> Unit,
        dbQuery: () -> T?,
        networkCall: suspend () -> Result<T>,
        saveCallResult: suspend (T) -> Unit,
        dispatcherProvider: DispatcherProvider
    ) {
        scope.launch(dispatcherProvider.io()) {
            emitValue(Result.loading())

            val dbRes = withContext(dispatcherProvider.main()) { dbQuery.invoke() }
            val call = withContext(dispatcherProvider.default()) { networkCall.invoke() }

            if (call.status == Result.Status.SUCCESS) {
                emitValue(call)
                saveCallResult(call.data!!)
            } else {
                emitValue(Result.error(call.message!!))

                if (dbRes != null) {
                    emitValue(Result.success(dbRes))
                }
            }
        }
    }

    protected fun <T> retrieveNetwork(
        scope: CoroutineScope,
        emitValue: (Result<T>) -> Unit,
        networkCall: suspend () -> Result<T>,
        dispatcherProvider: DispatcherProvider
    ) {
        scope.launch(dispatcherProvider.io()) {
            emitValue(Result.loading())

            val call =
                withContext(dispatcherProvider.default()) { networkCall.invoke() }

            if (call.status == Result.Status.SUCCESS) {
                emitValue(Result.success(call.data!!))
            } else {
                emitValue(Result.error(call.message!!))
            }
        }
    }
}