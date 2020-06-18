package io.dynamax.ios

import io.dynamax.home.HomeViewModel
import io.dynamax.home.di.homeDI
import org.kodein.di.direct
import org.kodein.di.instance

object DI {
    fun getHomeViewModel() = homeDI.direct.instance<HomeViewModel>()
}
