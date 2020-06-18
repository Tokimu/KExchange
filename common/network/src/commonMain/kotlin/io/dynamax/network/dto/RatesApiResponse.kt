package io.dynamax.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RatesApiResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)
