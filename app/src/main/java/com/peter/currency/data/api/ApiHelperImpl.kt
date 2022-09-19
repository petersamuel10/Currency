package com.peter.currency.data.api

import com.peter.currency.data.cache.CurrencyDao
import com.peter.currency.data.model.ConvertResponse
import com.peter.currency.data.model.Currency
import com.peter.currency.data.model.Historical
import com.peter.currency.util.NetworkHelper
import java.util.*
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val currencyDao: CurrencyDao,
    private val apiService: ApiService,
) : ApiHelper {

    override suspend fun getSymbols(): Currency {
        return apiService.getCurrency()
    }

    override suspend fun convert(to: String, from: String, amount: String): ConvertResponse {

        val result = apiService.convert(to, from, amount)
        currencyDao.insertConvertOp(
            Historical(
                fromCurrency = from,
                ToCurrency = to,
                amount = amount,
                result = result.result.toString(),
                date = Calendar.getInstance().time.toString()
            )
        )
        return result
    }

    override suspend fun getHistorical(): List<Historical> {
        return currencyDao.getHistorical()
    }
}