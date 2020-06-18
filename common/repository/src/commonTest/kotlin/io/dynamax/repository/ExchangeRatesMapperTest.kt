package io.dynamax.repository

import io.dynamax.model.Currency
import io.dynamax.network.dto.RatesApiResponse
import io.dynamax.repository.mapper.ExchangeRatesMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ExchangeRatesMapperTest {

    @Test
    fun `rates from api response to model`() {
        val mapper = ExchangeRatesMapper()
        val mockResponse = RatesApiResponse("EUR", mapOf("AUD" to 1.609, "BGN" to 1.1965, "BRL" to 4.259, "USD" to 1.131))
        val model = mapper.map(mockResponse)
        assertEquals(mockResponse.baseCurrency, model.baseCurrency.name)
        assertEquals(mockResponse.rates["USD"], model.rates[Currency.USD])
        assertFalse(model.rates.containsKey(Currency.EUR))
    }
}
