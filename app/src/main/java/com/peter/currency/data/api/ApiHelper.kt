package com.peter.currency.data.api

import com.peter.currency.data.model.Currency

interface ApiHelper {

    suspend fun getSymbols(): Currency
}