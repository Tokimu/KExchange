package io.dynamax.core.redux

import io.dynamax.core.redux.action.Fetch
import io.dynamax.core.redux.action.HomeAction.FetchCurrencies
import io.dynamax.core.utils.CoroutinesDispatcher
import io.dynamax.core.utils.dispatch
import io.dynamax.model.AppState
import io.dynamax.repository.HomeRepository
import kotlinx.coroutines.delay
import org.reduxkotlin.middleware

fun fetchMiddleware(dispatchers: CoroutinesDispatcher, homeRepository: HomeRepository) =
        middleware<AppState> { store, next, action ->
            if (action is FetchCurrencies && action.fetchAction is Fetch.Start) {
                next(action.copy(fetchAction = Fetch.Loading))
                dispatch(withScope = action.fetchAction.scope, mainDispatcher = dispatchers.mainDispatcher) {
                    do {
                        try {
                            val selectedRate = store.state.homeConverter.selectedCurrencyRate
                            val exchangeRates = homeRepository.getLatestCurrencies(dispatchers.ioDispatcher, selectedRate.name.toString())
                            next(action.copy(fetchAction = Fetch.Success(exchangeRates)))
                        } catch (e: Exception) {
                            next(action.copy(fetchAction = Fetch.Failure(e)))
                        }
                        delay(1000)
                    } while (action.pooling)
                }
            } else next(action)
        }
