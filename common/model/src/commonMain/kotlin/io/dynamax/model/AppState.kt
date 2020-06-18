package io.dynamax.model

data class AppState(
        val homeConverter: RatesConverter,
        val homeState: HomeViewState
)

sealed class ViewState

sealed class HomeViewState : ViewState() {
    object Empty: HomeViewState()
    object Loading: HomeViewState()
    data class Content(val rates: List<CurrencyRate>): HomeViewState()
    object Error: HomeViewState()
}

data class ExchangeRates(
        val baseCurrency: Currency,
        val rates: Map<Currency, Double>
)

data class RatesConverter(
        val selectedCurrencyRate: CurrencyRate,
        val rates: List<CurrencyRate>
)

data class CurrencyRate(
        val name: Currency,
        val value: Double,
        val rate: Double
)

enum class Currency(val fullName: String, val emojiCode: String) {
    AUD("Australian Dollar", "\uD83C\uDDE6\uD83C\uDDFA"),
    BGN("Bulgarian Lev", "\uD83C\uDDE7\uD83C\uDDEC"),
    BRL("Brazilian Real", "\uD83C\uDDE7\uD83C\uDDF7"),
    CAD("Canadian Dollar", "\uD83C\uDDE8\uD83C\uDDE6"),
    CHF("Swiss Franc", "\uD83C\uDDE8\uD83C\uDDED"),
    CNY("Chinese Yuan", "\uD83C\uDDE8\uD83C\uDDF3"),
    CZK("Czech Koruna", "\uD83C\uDDE8\uD83C\uDDFF"),
    DKK("Danish Krone", "\uD83C\uDDE9\uD83C\uDDF0"),
    EUR("Euro", "\uD83C\uDDEA\uD83C\uDDFA"),
    GBP("British Pound", "\uD83C\uDDEC\uD83C\uDDE7"),
    HKD("Hong Kong Dollar", "\uD83C\uDDED\uD83C\uDDF0"),
    HRK("Croatian Kuna", "\uD83C\uDDED\uD83C\uDDF7"),
    HUF("Hungarian Forint", "\uD83C\uDDED\uD83C\uDDFA"),
    IDR("Indonesian Rupiah", "\uD83C\uDDEE\uD83C\uDDE9"),
    ILS("Israeli New Shekel", "\uD83C\uDDEE\uD83C\uDDF1"),
    INR("Indian Rupee", "\uD83C\uDDEE\uD83C\uDDF3"),
    ISK("Icelandic Króna", "\uD83C\uDDEE\uD83C\uDDF8"),
    JPY("Japanese Yen", "\uD83C\uDDEF\uD83C\uDDF5"),
    KRW("South Korean Won", "\uD83C\uDDF0\uD83C\uDDF7"),
    MXN("Mexican Peso", "\uD83C\uDDF2\uD83C\uDDFD"),
    MYR("Malaysian Ringgit", "\uD83C\uDDF2\uD83C\uDDFE"),
    NOK("Norwegian Krone", "\uD83C\uDDF3\uD83C\uDDF4"),
    NZD("New Zealand Dollar", "\uD83C\uDDF3\uD83C\uDDFF"),
    PHP("Philippine Piso", "\uD83C\uDDF5\uD83C\uDDED"),
    PLN("Polish Zloty", "\uD83C\uDDF5\uD83C\uDDF1"),
    RON("Romanian Leu", "\uD83C\uDDF7\uD83C\uDDF4"),
    RUB("Russian Ruble", "\uD83C\uDDF7\uD83C\uDDFA"),
    SEK("Swedish Krona", "\uD83C\uDDF8\uD83C\uDDEA"),
    SGD("Singapore Dollar", "\uD83C\uDDF8\uD83C\uDDEC"),
    THB("Thai Baht", "\uD83C\uDDF9\uD83C\uDDED"),
    USD("US Dollar", "\uD83C\uDDFA\uD83C\uDDF8"),
    ZAR("South African Rand", "\uD83C\uDDFF\uD83C\uDDE6")
}
