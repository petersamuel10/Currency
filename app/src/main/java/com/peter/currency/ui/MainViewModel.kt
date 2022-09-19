package com.peter.currency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.currency.data.model.ConvertResponse
import com.peter.currency.data.model.Currency
import com.peter.currency.data.model.Historical
import com.peter.currency.data.repository.Repository
import com.peter.currency.util.NetworkHelper
import com.peter.currency.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: Repository,
) : ViewModel() {

    private val _symbolsData = MutableLiveData<Resource<Currency>>()
    val symbolsData: LiveData<Resource<Currency>>
        get() = _symbolsData

    private val _historicalData = MutableLiveData<Resource<List<Historical>>>()
    val historicalData: LiveData<Resource<List<Historical>>>
        get() = _historicalData

    private val _convertResult = MutableLiveData<Resource<ConvertResponse>>()
    val convertResult: LiveData<Resource<ConvertResponse>>
        get() = _convertResult

    fun getSymbols() {
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
    }

    fun convert(to: String, from: String, amount: String) {
        viewModelScope.launch {
            _convertResult.postValue(Resource.loading(null))
            try {
                repository.convert(to, from, amount).let {
                    _convertResult.postValue(Resource.success(it))
                }
            } catch (ex: Exception) {
                _convertResult.postValue(Resource.error(ex.message.toString(), null))
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