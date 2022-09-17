package com.peter.currency.data.model

data class Currency(
    val success: Boolean,
    val symbols: Symbols
)

data class Symbols(
    val AED: String,
    val AFN: String,
    val ALL: String,
    val AMD: String
)