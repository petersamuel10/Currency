package com.peter.currency.data.repository

import com.peter.currency.data.api.ApiHelper
import javax.inject.Inject

class Repository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getSymbols() = apiHelper.getSymbols()
    suspend fun convert(to: String, from: String, amount: String) =
        apiHelper.convert(to, from, amount)

    suspend fun getHistorical() = apiHelper.getHistorical()
}