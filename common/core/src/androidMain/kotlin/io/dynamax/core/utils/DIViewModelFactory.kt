package io.dynamax.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DI
import org.kodein.di.TT
import org.kodein.di.direct

class DIViewModelFactory(private val di: DI) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = di.direct.Instance(TT(modelClass))
}
