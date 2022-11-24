package com.example.currencyapp.network

import javax.inject.Inject


class ApiDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getConvertedRate(access_key: String, from: String, to: String, amount: Double) =
        apiService.convertCurrency(access_key, from, to, amount)

    suspend fun getConvertBaseCurrency(date: String,base: String, symbols:String) =
        apiService.convertBaseCurrency(date,base, symbols)

}