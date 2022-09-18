package com.peter.currency.data.repository

import com.peter.currency.data.api.ApiHelper
import javax.inject.Inject

class Repository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getSymbols() = apiHelper.getSymbols()
}