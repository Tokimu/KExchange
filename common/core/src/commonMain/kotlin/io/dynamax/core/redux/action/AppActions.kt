package io.dynamax.core.redux.action

import kotlinx.coroutines.CoroutineScope

interface Action

sealed class Fetch<out T> : Action {
    data class Start(val scope: CoroutineScope) : Fetch<Nothing>()
    object Loading : Fetch<Nothing>()
    data class Success<T>(val value: T) : Fetch<T>()
    data class Failure(val ex: Exception) : Fetch<Nothing>()
}
