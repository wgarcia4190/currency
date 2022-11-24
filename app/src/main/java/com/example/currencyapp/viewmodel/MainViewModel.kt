package com.example.currencyapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.helper.Resource
import com.example.currencyapp.helper.SingleLiveEvent
import com.example.currencyapp.model.ApiResponse
import com.example.currencyapp.model.BaseRatesResponse
import com.example.currencyapp.room.CurrencyEntity
import com.example.currencyapp.room.getDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(private val mainRepo: MainRepo, application: Application) : ViewModel() {
    var contextCompat: Context = application.baseContext

    private val _data = SingleLiveEvent<Resource<ApiResponse>>()
    private val _baseRatesData = SingleLiveEvent<Resource<BaseRatesResponse>>()
    val baseRatesData = _baseRatesData

    var ratesLiveData: LiveData<List<CurrencyEntity>>? = null

    val data = _data
    val convertedRate = MutableLiveData<Double>()

    fun getConvertedData(access_key: String, from: String, to: String, amount: Double) {
        viewModelScope.launch {
            mainRepo.getConvertedData(access_key, from, to, amount).collect {
                data.value = it
            }
        }
    }

    fun getConvertedBaseData(
        date: String, base: String, symbols: String
    ) {
        viewModelScope.launch {
            mainRepo.getConvertedBaseData(date, base, symbols).collect {
                baseRatesData.value = it
            }
        }
    }

    fun insertData(model: CurrencyEntity) {
        GlobalScope.launch {
            getDatabase(contextCompat).currencyDao().insertItem(model)
        }
    }

    fun getDataset() {
        viewModelScope.launch {
            ratesLiveData = getDatabase(contextCompat).currencyDao().getAllDataSet()
        }
    }
}