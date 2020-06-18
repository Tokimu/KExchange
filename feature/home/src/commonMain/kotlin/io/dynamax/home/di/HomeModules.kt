package io.dynamax.home.di

import io.dynamax.core.di.coreDI
import io.dynamax.core.utils.ViewStateMapper
import io.dynamax.home.HomeViewModel
import io.dynamax.home.HomeViewStateMapper
import io.dynamax.model.HomeViewState
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

private val homeStateMapperModule = DI.Module("HomeStateMapper") {
    bind<ViewStateMapper<HomeViewState>>() with provider { HomeViewStateMapper() }
}

private val homeViewModelModule = DI.Module("HomeViewModel") {
    bind<HomeViewModel>() with provider { HomeViewModel(homeStateMapper = instance()) }
}

val homeDI = DI {
    extend(coreDI)
    import(homeStateMapperModule)
    import(homeViewModelModule)
}
