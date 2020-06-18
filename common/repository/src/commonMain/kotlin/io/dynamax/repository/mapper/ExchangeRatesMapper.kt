package io.dynamax.repository.mapper

import io.dynamax.model.Currency
import io.dynamax.model.ExchangeRates
import io.dynamax.network.dto.RatesApiResponse

class ExchangeRatesMapper : Mapper<RatesApiResponse, ExchangeRates> {
    override fun map(t: RatesApiResponse): ExchangeRates {
        val baseCurrency = Currency.valueOf(t.baseCurrency)
        val rates = t.rates.mapKeys { Currency.valueOf(it.key) }
        return ExchangeRates(baseCurrency, rates)
    }
}
