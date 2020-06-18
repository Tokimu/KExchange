package io.dynamax.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.core.tag
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.graphics.Color
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.ConstraintSet
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.wrapContentHeight
import androidx.ui.livedata.observeAsState
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.ripple.ripple
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import io.dynamax.core.utils.DIViewModelFactory
import io.dynamax.home.HomeActivity
import io.dynamax.home.HomeViewModel
import io.dynamax.home.di.homeDI
import io.dynamax.model.CurrencyRate
import io.dynamax.model.HomeViewState.*
import org.kodein.di.DIAware

class MainActivity : AppCompatActivity(), DIAware {

    override val di = homeDI

    private val viewModel by viewModels<HomeViewModel> { DIViewModelFactory(di) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainView(viewModel, this)
            }
        }
    }
}

@Composable
fun MainView(vm: HomeViewModel, context: Context) {
    val homeViewState by vm.states.observeAsState(Empty)
    Column {
        ActionBar()
        when (homeViewState) {
            Empty -> {
            }
            Loading -> Loading()
            is Content -> {
                CurrencyConverter((homeViewState as Content).rates, vm)
            }
            Error -> ErrorView(context)
        }
    }
}

@Preview
@Composable
fun ActionBar() {
    TopAppBar(title = { Text(text = "Rates") }, backgroundColor = MaterialTheme.colors.background)
}

@Preview
@Composable
fun Loading() {
    Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalGravity = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = Color.Red)
    }
}

@Composable
fun CurrencyConverter(rates: List<CurrencyRate>, vm: HomeViewModel) {
    AdapterList(data = rates) {
        RateItem(it, vm)
    }
}

@Composable
fun RateItem(rate: CurrencyRate, vm: HomeViewModel) {
    ConstraintLayout(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().ripple().clickable(onClick = {
                vm.changeBase(rate)
            }),
            constraintSet = ConstraintSet {
                val flagConstraint = tag("flagTag").apply {
                    left constrainTo parent.left
                    left.margin = 16.dp
                    top.margin = 16.dp
                    bottom.margin = 16.dp
                    centerVertically()
                }
                tag("labelsTag").apply {
                    left constrainTo flagConstraint.right
                    left.margin = 16.dp
                    top constrainTo flagConstraint.top
                    bottom constrainTo flagConstraint.bottom
                }

                tag("amountTag").apply {
                    left constrainTo parent.left
                    right constrainTo parent.right
                    right.margin = 16.dp
                    horizontalBias = 1.0f
                    centerVertically()
                }
            }
    ) {
        Text(
                modifier = Modifier.tag("flagTag"),
                fontSize = TextUnit.Sp(40),
                text = rate.name.emojiCode
        )

        Column(Modifier.tag("labelsTag")) {
            Text(
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    text = rate.name.toString()
            )
            Text(
                    text = rate.name.fullName
            )
        }

        Text(
                modifier = Modifier.tag("amountTag"),
                fontSize = TextUnit.Sp(16),
                text = rate.value.toString()
        )
    }
}

@Composable
fun ErrorView(context: Context) {
    val typography = MaterialTheme.typography

    Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalGravity = Alignment.CenterHorizontally) {
        Text(
                modifier = Modifier.padding(8.dp),
                style = typography.h5,
                text = context.getString(R.string.no_data_label)
        )
        Text(
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                style = typography.h6,
                text = context.getString(R.string.check_connection)
        )
    }
}
