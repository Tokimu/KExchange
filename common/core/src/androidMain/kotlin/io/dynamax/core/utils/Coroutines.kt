package io.dynamax.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

actual fun dispatch(withScope: CoroutineScope, mainDispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> Unit) {
    withScope.launch(mainDispatcher, block = block)
}

internal actual class CoroutinesDispatcherImpl actual constructor() : CoroutinesDispatcher {
    actual override val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    actual override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
}
