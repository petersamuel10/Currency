package com.peter.currency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.currency.data.model.Currency
import com.peter.currency.data.model.Historical
import com.peter.currency.data.repository.Repository
import com.peter.currency.util.NetworkHelper
import com.peter.currency.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: Repository,
) : ViewModel() {

    var fromAmount = MutableStateFlow("1")
    var toAmount = MutableStateFlow("")

    var fromCurrency = MutableStateFlow(0)
    var toCurrency = MutableStateFlow(0)

    private val _symbolsData = MutableLiveData<Resource<Currency>>()
    val symbolsData: LiveData<Resource<Currency>>
        get() = _symbolsData

    private val _convertData = MutableLiveData<Resource<String>>()
    val convertData: LiveData<Resource<String>>
        get() = _convertData

    private val _historicalData = MutableLiveData<Resource<List<Historical>>>()
    val historicalData: LiveData<Resource<List<Historical>>>
        get() = _historicalData


    fun getSymbols() {
        if (networkHelper.isNetworkConnected())
            viewModelScope.launch {
                _symbolsData.postValue(Resource.loading(null))
                try {
                    repository.getSymbols().let {
                        _symbolsData.postValue(Resource.success(it))
                    }
                } catch (ex: Exception) {
                    _symbolsData.postValue(Resource.error(ex.message.toString(), null))
                }
            }
        else
            _symbolsData.postValue(Resource.error("No Internet connection", null))

    }

    fun convert(to: String, from: String) {

        if (networkHelper.isNetworkConnected())
            viewModelScope.launch {
                _convertData.postValue(Resource.loading(null))
                try {
                    repository.convert(to, from, fromAmount.value).let {
                        _convertData.postValue(Resource.success(it.result.toString()))
                        toAmount.value = it.result.toString()
                    }
                } catch (ex: Exception) {
                    _convertData.postValue(Resource.error(ex.message.toString(), null))
                }
            }
        else
            _convertData.postValue(Resource.error("No Internet connection", null))
        if (networkHelper.isNetworkConnected())
            viewModelScope.launch {
                repository.convert(to, from, fromAmount.value).let {
                    toAmount.value = it.result.toString()
                }
            }
    }

    fun getHistorical() {
        viewModelScope.launch {
            _historicalData.postValue(Resource.loading(null))
            try {
                repository.getHistorical().let {
                    _historicalData.postValue(Resource.success(it))
                }
            } catch (ex: Exception) {
                _historicalData.postValue(Resource.error(ex.message.toString(), null))
            }
        }
    }
}