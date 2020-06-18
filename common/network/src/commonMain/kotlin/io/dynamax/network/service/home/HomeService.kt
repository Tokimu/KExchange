package io.dynamax.network.service.home

import io.dynamax.network.client.HttpClient
import io.dynamax.network.dto.RatesApiResponse
import io.ktor.client.request.get
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import io.ktor.client.request.url

class HomeService(private val httpClient: HttpClient) {

    suspend fun getLatestCurrencies(context: CoroutineContext, base: String) = withContext(context) {
        httpClient.client.get<RatesApiResponse> { url("${httpClient.baseURL}/latest?base=$base") }
    }
}
