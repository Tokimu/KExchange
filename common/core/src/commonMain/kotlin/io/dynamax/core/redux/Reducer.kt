package io.dynamax.core.redux

import io.dynamax.model.AppState
import org.reduxkotlin.Reducer

val mainReducers: Reducer<AppState> = { state, _ -> state }

