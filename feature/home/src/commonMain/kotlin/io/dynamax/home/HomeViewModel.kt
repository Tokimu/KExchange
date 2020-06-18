package io.dynamax.home

import io.dynamax.core.redux.action.Action
import io.dynamax.core.redux.action.Converter.calculateValues
import io.dynamax.core.redux.action.Converter.changeConverterBase
import io.dynamax.core.redux.action.Converter.updateConverterRates
import io.dynamax.core.redux.action.Fetch
import io.dynamax.core.redux.action.HomeAction.CalculateExchange
import io.dynamax.core.redux.action.HomeAction.ChangeConverterBase
import io.dynamax.core.redux.action.HomeAction.FetchCurrencies
import io.dynamax.core.utils.BaseViewModel
import io.dynamax.core.utils.ViewStateMapper
import io.dynamax.model.AppState
import io.dynamax.model.CurrencyRate
import io.dynamax.model.HomeViewState
import org.reduxkotlin.reducerForActionType

val homeReducer = reducerForActionType<AppState, Action> { state, action ->
    when (action) {
        is FetchCurrencies -> {
            when (val fetch = action.fetchAction) {
                Fetch.Loading -> state.copy(homeState = HomeViewState.Loading)
                is Fetch.Success -> {
                    val converter = updateConverterRates(state.homeConverter, fetch.value)
                    state.copy(homeConverter = converter, homeState = HomeViewState.Content(rates = converter.rates))
                }
                is Fetch.Failure -> {
                    if (state.homeConverter.rates.isEmpty())
                        state.copy(homeState = HomeViewState.Error)
                    else state.copy(homeState = HomeViewState.Content(rates = state.homeConverter.rates))
                }
                else -> state
            }
        }
        is ChangeConverterBase -> {
            val converter = changeConverterBase(state.homeConverter, action.newBase)
            state.copy(homeConverter = converter, homeState = HomeViewState.Content(rates = converter.rates))
        }
        is CalculateExchange -> {
            val converter = calculateValues(state.homeConverter, action.newBaseValue)
            state.copy(homeConverter = converter, homeState = HomeViewState.Content(rates = converter.rates))
        }
        else -> state
    }
}

class HomeViewModel(homeStateMapper: ViewStateMapper<HomeViewState>) : BaseViewModel<HomeViewState>(homeStateMapper, homeReducer) {

    init {
        store.dispatch(FetchCurrencies(pooling = true, fetchAction = Fetch.Start(viewModelScope)))
    }

    fun changeBase(newBase: CurrencyRate) {
        store.dispatch(ChangeConverterBase(newBase))
    }

    fun calculateExchange(value: Double) {
        store.dispatch(CalculateExchange(value))
    }
}
