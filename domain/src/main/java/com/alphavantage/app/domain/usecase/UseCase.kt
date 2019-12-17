package com.alphavantage.app.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.alphavantage.app.domain.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class UseCase {

    @Deprecated("Sementara belum dipakai, scope livedata")
    protected fun <T> retrieveNetworkAndSync(
        dbQuery: () -> T?,
        networkCall: suspend () -> Result<T>,
        saveCallResult: suspend (T) -> Unit
    ): LiveData<Result<T>> = liveData(Dispatchers.IO) {
        emit(Result.loading())

        val dbRes = dbQuery.invoke()
        val liveData = MutableLiveData<Result<T>>()

        val callRes = networkCall.invoke()
        if (callRes.status == Result.Status.SUCCESS) {
            liveData.postValue(callRes)
            emitSource(liveData)

            saveCallResult(callRes.data!!)
        } else {
            if (dbRes != null) {
                liveData.postValue(Result.success(dbRes))
                emitSource(liveData)
            }

            emit(Result.error(callRes.message!!))
        }
    }

    @Deprecated("Sementara belum dipakai, scope livedata")
    protected fun <T> retrieveNetwork(
        networkCall: suspend () -> Result<T>
    ): LiveData<Result<T>> =
        liveData(Dispatchers.IO) {
            emit(Result.loading())

            val callStatus = networkCall.invoke()
            val callLiveData: MutableLiveData<Result<T>> = MutableLiveData()

            if (callStatus.status == Result.Status.SUCCESS) {
                callLiveData.postValue(callStatus)
                emitSource(callLiveData)
            } else {
                emit(Result.error(callStatus.message!!))
            }
        }

    protected fun <T> retrieveNetworkAndSync(
        scope: CoroutineScope,
        emitValue: (Result<T>) -> Unit,
        dbQuery: () -> T?,
        networkCall: suspend () -> Result<T>,
        saveCallResult: suspend (T) -> Unit
    ) {
        scope.launch {
            emitValue(Result.loading())

            val dbRes = dbQuery.invoke()
            val call = networkCall.invoke()

            if (call.status == Result.Status.SUCCESS) {
                emitValue(call)
                saveCallResult(call.data!!)
            } else {
                emitValue(Result.error(call.message!!))

                if (dbRes != null) {
                    emitValue(call)
                }
            }
        }
    }

    protected fun <T> retrieveNetwork(
        scope: CoroutineScope,
        emitValue: (Result<T>) -> Unit,
        networkCall: suspend () -> Result<T>
    ) {
        scope.launch(Dispatchers.Default) {
            emitValue(Result.loading())

            val call = networkCall.invoke()

            if (call.status == Result.Status.SUCCESS) {
                emitValue(call)
            } else {
                emitValue(Result.error(call.message!!))
            }
        }
    }
}