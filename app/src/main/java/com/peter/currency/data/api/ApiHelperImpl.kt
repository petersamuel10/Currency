package com.peter.currency.data.api

import com.peter.currency.data.model.Currency
import com.peter.currency.util.NetworkHelper
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val apiService: ApiService,
) : ApiHelper {

    override suspend fun getSymbols(): Currency {
        return apiService.getCurrency()
    }
}