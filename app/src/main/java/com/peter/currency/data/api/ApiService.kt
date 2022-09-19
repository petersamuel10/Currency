package com.peter.currency.data.api

import com.peter.currency.data.model.ConvertResponse
import com.peter.currency.data.model.Currency
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("symbols")
    suspend fun getCurrency(): Currency

    @GET("convert")
    suspend fun convert(@Query("to") to:String,@Query("from") from:String,@Query("amount") amount:String): ConvertResponse
}