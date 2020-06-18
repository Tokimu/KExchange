package io.dynamax.core.utils

import io.dynamax.model.AppState
import io.dynamax.model.ViewState

interface ViewStateMapper<out U : ViewState> {
    fun map(t: AppState): U
}
