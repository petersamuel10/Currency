package com.peter.currency.data.api

import com.peter.currency.data.model.ConvertResponse
import com.peter.currency.data.model.Currency
import com.peter.currency.data.model.Historical

interface ApiHelper {

    suspend fun getSymbols(): Currency
    suspend fun convert(to:String, from:String, amount:String): ConvertResponse
    suspend fun getHistorical(): List<Historical>
}