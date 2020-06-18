package io.dynamax.repository.di

import io.dynamax.network.di.networkDI
import io.dynamax.repository.HomeRepository
import io.dynamax.repository.HomeRepositoryImpl
import io.dynamax.repository.mapper.ExchangeRatesMapper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

private val homeRepositoryModule = DI.Module("HomeRepository") {
    bind<HomeRepository>() with provider { HomeRepositoryImpl(homeService = instance(), exchangeRatesMapper = instance()) }
}

private val homeMapperModule = DI.Module("HomeMapper") {
    bind<ExchangeRatesMapper>() with provider { ExchangeRatesMapper() }
}

val repositoryDI = DI {
    extend(networkDI)
    import(homeRepositoryModule)
    import(homeMapperModule)
}
