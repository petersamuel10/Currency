package com.peter.currency.di

import android.content.Context
import com.peter.currency.BuildConfig
import com.peter.currency.R
import com.peter.currency.base.App
import com.peter.currency.data.api.ApiHelper
import com.peter.currency.data.api.ApiHelperImpl
import com.peter.currency.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): App {
        return context as App
    }

    @Provides
    @Singleton
    fun Interceptor(context: Context) = Interceptor { chain ->
        val request =
            chain.request().newBuilder()
                .header("apikey", context.resources.getString(R.string.api_key))
                .build()
        chain.proceed(request)
    }

    @Provides
    fun provideBaseUrl() = "https://api.apilayer.com/fixer/"


    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, baseURL: String) =
        Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}