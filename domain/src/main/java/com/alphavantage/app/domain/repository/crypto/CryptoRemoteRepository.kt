package com.alphavantage.app.domain.repository.crypto

import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.crypto.CryptoSeries

interface CryptoRemoteRepository {

    suspend fun getMonthlyRate(symbol: String, market: String): Result<CryptoSeries>
}