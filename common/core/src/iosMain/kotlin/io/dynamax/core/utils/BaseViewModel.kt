package io.dynamax.core.utils

import io.dynamax.core.di.coreDI
import io.dynamax.core.redux.mainReducers
import io.dynamax.model.AppState
import io.dynamax.model.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.reduxkotlin.Reducer
import org.reduxkotlin.Store
import org.reduxkotlin.StoreSubscription
import org.reduxkotlin.combineReducers

actual abstract class BaseViewModel<T : ViewState> actual constructor(
        private val viewStateMapper: ViewStateMapper<T>,
        reducer: Reducer<AppState>
) : DIAware {

    override val di: DI get() = coreDI

    private lateinit var subscription: StoreSubscription
    private val viewModelJob = SupervisorJob()
    private val coroutinesDispatchers by instance<CoroutinesDispatcher>()

    protected actual val store by instance<Store<AppState>>()
    protected actual val viewModelScope = CoroutineScope(coroutinesDispatchers.mainDispatcher + viewModelJob)

    init {
        store.replaceReducer(combineReducers(mainReducers, reducer))
    }

    fun states(subscriber: (T) -> Unit) {
        subscriber(viewStateMapper.map(store.state))

        subscription = store.subscribe {
            subscriber(viewStateMapper.map(store.state))
        }
    }

    actual val state: T
        get() = viewStateMapper.map(store.state)

    actual fun onCleared() {
        subscription()
        viewModelJob.cancelChildren()
    }
}
