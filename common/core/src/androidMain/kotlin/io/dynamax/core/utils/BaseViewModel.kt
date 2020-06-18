package io.dynamax.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.dynamax.core.di.coreDI
import io.dynamax.core.redux.mainReducers
import io.dynamax.model.AppState
import io.dynamax.model.ViewState
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.reduxkotlin.Reducer
import org.reduxkotlin.Store
import org.reduxkotlin.StoreSubscription
import org.reduxkotlin.combineReducers
import androidx.lifecycle.viewModelScope as lifecycleViewModelScope

actual abstract class BaseViewModel<T : ViewState> actual constructor(
        private val viewStateMapper: ViewStateMapper<T>,
        reducer: Reducer<AppState>
) : ViewModel(), DIAware {

    override val di: DI get() = coreDI

    private lateinit var subscription: StoreSubscription

    protected actual val store by instance<Store<AppState>>()
    protected actual val viewModelScope = lifecycleViewModelScope

    init {
        store.replaceReducer(combineReducers(mainReducers, reducer))
    }

    val states: LiveData<T> by lazy {
        Transformations.distinctUntilChanged(
                MutableLiveData<T>().apply {
                    postValue(viewStateMapper.map(store.getState()))

                    subscription = store.subscribe {
                        postValue(viewStateMapper.map(store.getState()))
                    }
                }
        )
    }

    actual val state: T
        get() = viewStateMapper.map(store.getState())

    actual override fun onCleared() {
        subscription()
        super.onCleared()
    }
}
