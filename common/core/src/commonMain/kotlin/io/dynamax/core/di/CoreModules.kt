package io.dynamax.core.di

import io.dynamax.core.redux.createStore
import io.dynamax.core.utils.CoroutinesDispatcher
import io.dynamax.core.utils.CoroutinesDispatcherImpl
import io.dynamax.model.AppState
import io.dynamax.repository.di.repositoryDI
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.reduxkotlin.Store

private val coroutineDispatcherModule = DI.Module("CoroutinesDispatcher") {
    bind<CoroutinesDispatcher>() with provider { CoroutinesDispatcherImpl() }
}

private val reduxModule = DI.Module("ReduxStore") {
    bind<Store<AppState>>() with singleton { createStore(instance(), instance()) }
}

val coreDI = DI {
    extend(repositoryDI)
    import(reduxModule)
    import(coroutineDispatcherModule)
}
