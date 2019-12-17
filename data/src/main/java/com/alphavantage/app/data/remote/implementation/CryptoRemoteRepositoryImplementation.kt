package com.alphavantage.app.data.remote.implementation

import com.alphavantage.app.data.remote.api.CryptoService
import com.alphavantage.app.data.remote.mapper.crypto.CryptoMapper
import com.alphavantage.app.domain.model.Result
import com.alphavantage.app.domain.model.crypto.CryptoSeries
import com.alphavantage.app.domain.repository.crypto.CryptoRemoteRepository
import org.koin.dsl.module

class CryptoRemoteRepositoryImplementation(private val service: CryptoService) : BaseImplementation(), CryptoRemoteRepository {

    override suspend fun getMonthlyRate(symbol: String, market: String): Result<CryptoSeries> =
        getResult(
            { service.getMonthlySeries(symbol, market) },
            { CryptoMapper.mapSeries(it) }
        )
}