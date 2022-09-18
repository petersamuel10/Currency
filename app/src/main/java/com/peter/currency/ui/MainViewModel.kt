package com.peter.currency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.currency.data.model.Currency
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
}