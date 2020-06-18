package io.dynamax.core.redux

import io.dynamax.core.utils.CoroutinesDispatcher
import io.dynamax.model.AppState
import io.dynamax.model.Currency
import io.dynamax.model.CurrencyRate
import io.dynamax.model.HomeViewState
import io.dynamax.model.RatesConverter
import io.dynamax.repository.HomeRepository
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createSameThreadEnforcedStore

val initialState = AppState(
        homeConverter = RatesConverter(selectedCurrencyRate = CurrencyRate(Currency.EUR, 1.0, 1.0), rates = emptyList()),
        homeState = HomeViewState.Empty
)

internal fun createStore(dispatchers: CoroutinesDispatcher, homeRepository: HomeRepository) = createSameThreadEnforcedStore(
        mainReducers,
        initialState,
        applyMiddleware(fetchMiddleware(dispatchers, homeRepository))
)
