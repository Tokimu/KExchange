package io.dynamax.core.utils

import io.dynamax.model.AppState
import io.dynamax.model.ViewState
import kotlinx.coroutines.CoroutineScope
import org.reduxkotlin.Reducer
import org.reduxkotlin.Store

expect abstract class BaseViewModel<T : ViewState>(viewStateMapper: ViewStateMapper<T>, reducer: Reducer<AppState>) {
    protected val store: Store<AppState>

    protected val viewModelScope: CoroutineScope

    val state: T

    protected fun onCleared()
}
