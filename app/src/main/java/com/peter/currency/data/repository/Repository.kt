package com.peter.currency.data.repository

import com.peter.currency.data.api.ApiHelper
import javax.inject.Inject

class repository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getSymbols() = apiHelper.getSymbols()
}