package com.example.currencyapp.viewmodel

import com.example.currencyapp.helper.Resource
import com.example.currencyapp.model.ApiResponse
import com.example.currencyapp.model.BaseRatesResponse
import com.example.currencyapp.network.ApiDataSource
import com.example.currencyapp.network.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class MainRepo @Inject constructor(
    private val apiDataSource: ApiDataSource,
) : BaseDataSource() {

    //Using coroutines flow to get the response from
    suspend fun getConvertedData(
        access_key: String,
        from: String,
        to: String,
        amount: Double
    ): Flow<Resource<ApiResponse>> {
        return flow {
            emit(safeApiCall { apiDataSource.getConvertedRate(access_key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getConvertedBaseData(
        date: String,base: String, symbols: String
    ): Flow<Resource<BaseRatesResponse>> {
        return flow {
            emit(safeApiCall { apiDataSource.getConvertBaseCurrency(date, base,symbols) })
        }.flowOn(Dispatchers.IO)
    }
}