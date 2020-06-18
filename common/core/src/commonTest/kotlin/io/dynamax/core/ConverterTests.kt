package io.dynamax.core

import io.dynamax.core.redux.action.Converter
import io.dynamax.model.Currency.AUD
import io.dynamax.model.Currency.BRL
import io.dynamax.model.Currency.CHF
import io.dynamax.model.Currency.EUR
import io.dynamax.model.Currency.JPY
import io.dynamax.model.CurrencyRate
import io.dynamax.model.ExchangeRates
import io.dynamax.model.RatesConverter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ConverterTests {

    @Test
    fun `update rates with empty state should be correct`() {
        val emptyConverter = RatesConverter(CurrencyRate(CHF, 1.0, 1.0), emptyList())
        val updatedRates = ExchangeRates(CHF, mapOf(JPY to 110.0, EUR to 0.88, AUD to 1.4, BRL to 3.75))
        val result = Converter.updateConverterRates(emptyConverter, updatedRates)
        assertEquals(result.selectedCurrencyRate.name, updatedRates.baseCurrency)
        assertEquals(result.rates.size, 5)
        assertTrue(result.rates.contains(result.selectedCurrencyRate))
        assertEquals(updatedRates.rates[JPY], result.rates[1].rate)
    }

    @Test
    fun `update rates should be correct`() {
        val currentConverter = RatesConverter(
                selectedCurrencyRate = CurrencyRate(CHF, 1.0, 1.0),
                rates = listOf(
                        CurrencyRate(CHF, 1.0, 1.0),
                        CurrencyRate(JPY, 115.0, 115.0),
                        CurrencyRate(EUR, 0.90, 0.90),
                        CurrencyRate(AUD, 1.4, 1.4),
                        CurrencyRate(BRL, 4.0, 4.0)
                )
        )
        val updatedRates = ExchangeRates(CHF, mapOf(JPY to 110.0, EUR to 0.88, AUD to 1.4, BRL to 3.75))
        val result = Converter.updateConverterRates(currentConverter, updatedRates)
        assertEquals(result.selectedCurrencyRate.name, updatedRates.baseCurrency)
        assertEquals(result.rates.size, 5)
        assertTrue(result.rates.contains(result.selectedCurrencyRate))
        assertEquals(updatedRates.rates[JPY], result.rates[1].rate)
        assertEquals(updatedRates.rates[BRL], result.rates.last().rate)
    }

    @Test
    fun `update rates with different base shouldn't fail`() {
        val currentConverter = RatesConverter(
                selectedCurrencyRate = CurrencyRate(CHF, 1.0, 1.0),
                rates = listOf(
                        CurrencyRate(CHF, 1.0, 1.0),
                        CurrencyRate(JPY, 115.0, 115.0),
                        CurrencyRate(EUR, 0.90, 0.90),
                        CurrencyRate(AUD, 1.4, 1.4),
                        CurrencyRate(BRL, 4.0, 4.0)
                )
        )
        val updatedRates = ExchangeRates(JPY, mapOf(CHF to 1.0, EUR to 0.88, AUD to 1.4, BRL to 3.75))
        val result = Converter.updateConverterRates(currentConverter, updatedRates)
        assertNotEquals(currentConverter.selectedCurrencyRate.name, updatedRates.baseCurrency)
        assertEquals(result.selectedCurrencyRate.name, result.selectedCurrencyRate.name)
        assertEquals(result.rates, result.rates)
    }

    @Test
    fun `change converter base should be correct`() {
        val currentConverter = RatesConverter(
                selectedCurrencyRate = CurrencyRate(CHF, 1.0, 1.0),
                rates = listOf(
                        CurrencyRate(CHF, 1.0, 1.0),
                        CurrencyRate(JPY, 115.0, 115.0),
                        CurrencyRate(EUR, 0.90, 0.90),
                        CurrencyRate(AUD, 1.4, 1.4),
                        CurrencyRate(BRL, 4.0, 4.0)
                )
        )
        val newBase = CurrencyRate(BRL, 4.0, 4.0)
        val result = Converter.changeConverterBase(currentConverter, newBase)
        assertEquals(result.selectedCurrencyRate.name, newBase.name)
        assertEquals(result.rates.size, 5)
        assertTrue(result.rates.contains(result.selectedCurrencyRate))
        assertEquals(result.rates.first().name, newBase.name)
        assertEquals(result.rates[1].value, 1.0)
        assertEquals(result.rates.last().name, AUD)
    }

    @Test
    fun `converter base change to same one should return old converter`() {
        val currentConverter = RatesConverter(
                selectedCurrencyRate = CurrencyRate(CHF, 1.0, 1.0),
                rates = listOf(
                        CurrencyRate(CHF, 1.0, 1.0),
                        CurrencyRate(JPY, 115.0, 115.0),
                        CurrencyRate(EUR, 0.90, 0.90),
                        CurrencyRate(AUD, 1.4, 1.4),
                        CurrencyRate(BRL, 4.0, 4.0)
                )
        )
        val newBase = CurrencyRate(CHF, 1.0, 1.0)
        val result = Converter.changeConverterBase(currentConverter, newBase)
        assertEquals(currentConverter, result)
    }

    @Test
    fun `updating base value should update all rates values`() {
        val currentConverter = RatesConverter(
                selectedCurrencyRate = CurrencyRate(CHF, 1.0, 1.0),
                rates = listOf(
                        CurrencyRate(CHF, 1.0, 1.0),
                        CurrencyRate(JPY, 115.0, 115.0),
                        CurrencyRate(EUR, 0.90, 0.90),
                        CurrencyRate(AUD, 1.4, 1.4),
                        CurrencyRate(BRL, 4.0, 4.0)
                )
        )
        val newValue = 3.0
        val result = Converter.calculateValues(currentConverter, newValue)
        assertEquals(result.selectedCurrencyRate.value, 3.0)
        assertEquals(result.rates.first().value, 3.0)
        assertEquals(result.rates[1].value, 345.0)
        assertEquals(result.rates.last().value, 12.0)
        assertEquals(result.rates.last().rate, 4.0)
    }
}
