package io.dynamax.core.redux.action

import io.dynamax.model.CurrencyRate
import io.dynamax.model.ExchangeRates
import io.dynamax.model.RatesConverter
import kotlin.math.round

sealed class HomeAction : Action {
    data class FetchCurrencies(val pooling: Boolean, val fetchAction: Fetch<ExchangeRates>) : HomeAction()
    data class ChangeConverterBase(val newBase: CurrencyRate) : HomeAction()
    data class CalculateExchange(val newBaseValue: Double) : HomeAction()
}

object Converter {
    fun updateConverterRates(ratesConverter: RatesConverter, updatedData: ExchangeRates): RatesConverter {
        if (ratesConverter.selectedCurrencyRate.name != updatedData.baseCurrency) return ratesConverter
        return if (ratesConverter.rates.isEmpty()) {
            val baseRate = CurrencyRate(updatedData.baseCurrency, 1.0, 1.0)
            val rates = listOf(baseRate) + (updatedData.rates.map { CurrencyRate(it.key, it.value, it.value) })
            RatesConverter(baseRate, rates)
        } else {
            val newRates = ratesConverter.rates.map {
                val newRate = (updatedData.rates[it.name] ?: it.rate)
                val newValue = if (it.name != ratesConverter.selectedCurrencyRate.name) {
                    (ratesConverter.selectedCurrencyRate.value * newRate).round(2)
                } else it.value
                it.copy(value = newValue, rate = newRate)
            }
            RatesConverter(ratesConverter.selectedCurrencyRate, newRates)
        }
    }

    fun changeConverterBase(ratesConverter: RatesConverter, newBase: CurrencyRate): RatesConverter {
        if (newBase.name == ratesConverter.selectedCurrencyRate.name) return ratesConverter
        val oldBaseRate = ratesConverter.rates.first().rate / newBase.rate
        val oldBase = ratesConverter.rates.first().copy(rate = oldBaseRate)
        val rates = (listOf(newBase.copy(rate = newBase.value), oldBase) + ratesConverter.rates).distinctBy { it.name }
        return RatesConverter(newBase, rates)
    }

    fun calculateValues(ratesConverter: RatesConverter, newValue: Double): RatesConverter {
        val newBase = ratesConverter.selectedCurrencyRate.copy(value = newValue, rate = newValue)
        val newRates = ratesConverter.rates.map {
            if (it.name != ratesConverter.selectedCurrencyRate.name)
                it.copy(value = (newValue * it.rate).round(2))
            else newBase
        }
        return RatesConverter(newBase, newRates)
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}
