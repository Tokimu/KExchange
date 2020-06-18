package io.dynamax.home

import io.dynamax.core.utils.ViewStateMapper
import io.dynamax.model.AppState
import io.dynamax.model.HomeViewState

class HomeViewStateMapper : ViewStateMapper<HomeViewState> {
    override fun map(t: AppState) = t.homeState
}
