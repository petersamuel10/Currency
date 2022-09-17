package com.peter.currency.data.api

import com.peter.currency.data.model.Currency
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("symbols")
    suspend fun getCurrency(): Currency
}