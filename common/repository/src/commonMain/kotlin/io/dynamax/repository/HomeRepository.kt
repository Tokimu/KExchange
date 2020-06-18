package io.dynamax.repository

import io.dynamax.model.ExchangeRates
import io.dynamax.network.service.home.HomeService
import io.dynamax.repository.mapper.ExchangeRatesMapper
import kotlin.coroutines.CoroutineContext


interface HomeRepository {
    suspend fun getLatestCurrencies(context: CoroutineContext, base: String): ExchangeRates
}

class HomeRepositoryImpl(private val homeService: HomeService, private val exchangeRatesMapper: ExchangeRatesMapper) : HomeRepository {
    override suspend fun getLatestCurrencies(context: CoroutineContext, base: String): ExchangeRates {
        val result = homeService.getLatestCurrencies(context, base)
        return exchangeRatesMapper.map(result)
    }
}
