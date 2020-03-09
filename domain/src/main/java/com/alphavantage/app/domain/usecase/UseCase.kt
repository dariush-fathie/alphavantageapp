package com.alphavantage.app.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.alphavantage.app.domain.model.Result
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
        saveCallResult: suspend (T) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            emitValue(Result.loading())

            val dbRes = withContext(Dispatchers.Main) { dbQuery.invoke() }
            val call = withContext(Dispatchers.Default) { networkCall.invoke() }

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
        networkCall: suspend () -> Result<T>
    ) {
        scope.launch(Dispatchers.IO) {
            emitValue(Result.loading())

            val call =
                withContext(Dispatchers.Default) { networkCall.invoke() }

            if (call.status == Result.Status.SUCCESS) {
                emitValue(call)
            } else {
                emitValue(Result.error(call.message!!))
            }
        }
    }
}