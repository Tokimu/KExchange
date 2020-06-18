package io.dynamax.network.di

import io.dynamax.network.client.HttpClient
import io.dynamax.network.service.home.HomeService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

private val httpClientModule = DI.Module("HttpClient") {
    bind<HttpClient>() with singleton { HttpClient() }
}

private val homeServiceModule = DI.Module("HomeService") {
    bind<HomeService>() with provider { HomeService(httpClient = instance()) }
}

val networkDI = DI {
    import(httpClientModule)
    import(homeServiceModule)
}
