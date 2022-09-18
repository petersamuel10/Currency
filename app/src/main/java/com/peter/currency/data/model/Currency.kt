package com.peter.currency.data.model

data class Currency(
    val success: Boolean,
    val symbols: HashMap<String, String>
)