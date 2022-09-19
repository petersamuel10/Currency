package com.peter.currency.data.api

import com.peter.currency.data.model.ConvertResponse
import com.peter.currency.data.model.Currency

interface ApiHelper {

    suspend fun getSymbols(): Currency
    suspend fun convert(to:String, from:String, amount:String): ConvertResponse
}