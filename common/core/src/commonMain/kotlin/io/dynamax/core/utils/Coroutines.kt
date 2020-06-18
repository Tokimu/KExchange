package io.dynamax.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

expect fun dispatch(withScope: CoroutineScope = GlobalScope, mainDispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> Unit)

interface CoroutinesDispatcher {
    val mainDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
}

internal expect class CoroutinesDispatcherImpl() : CoroutinesDispatcher {
    override val mainDispatcher: CoroutineDispatcher
    override val ioDispatcher: CoroutineDispatcher
}
