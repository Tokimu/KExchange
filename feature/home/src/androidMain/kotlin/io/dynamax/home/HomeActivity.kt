package io.dynamax.home

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import io.dynamax.core.utils.DIViewModelFactory
import io.dynamax.home.databinding.ActivityHomeBinding
import io.dynamax.home.di.homeDI
import io.dynamax.model.HomeViewState
import org.kodein.di.DI
import org.kodein.di.DIAware

class HomeActivity : AppCompatActivity(), DIAware {

    override val di: DI get() = homeDI

    private val viewModel by viewModels<HomeViewModel> { DIViewModelFactory(di) }

    private val ratesAdapter by lazy {
        RatesAdapter(
            onItemClick = { viewModel.changeBase(it) },
            onCurrencyAmountChanged = { viewModel.calculateExchange(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            (ratesList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            ratesList.animation = null
            ratesList.setHasFixedSize(true)
            ratesList.adapter = ratesAdapter

            viewModel.states.observe(this@HomeActivity, Observer {
                when (it) {
                    HomeViewState.Empty -> {
                        ratesList.visibility = View.GONE
                        errorView.root.visibility = View.GONE
                        loading.visibility = View.GONE
                    }
                    HomeViewState.Loading -> {
                        ratesList.visibility = View.GONE
                        errorView.root.visibility = View.GONE
                        loading.visibility = View.VISIBLE
                    }
                    is HomeViewState.Content -> {
                        errorView.root.visibility = View.GONE
                        loading.visibility = View.GONE
                        ratesList.visibility = View.VISIBLE
                        ratesAdapter.setRates(it.rates)
                    }
                    HomeViewState.Error -> {
                        ratesList.visibility = View.GONE
                        loading.visibility = View.GONE
                        errorView.root.visibility = View.VISIBLE
                    }
                }
            })
        }
    }
}
