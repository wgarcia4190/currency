package com.example.currencyapp.network

import com.example.currencyapp.helper.EndPoints
import com.example.currencyapp.model.ApiResponse
import com.example.currencyapp.model.BaseRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("apikey: ${EndPoints.API_KEY}")
    @GET(EndPoints.CONVERT_URL)
    suspend fun convertCurrency(
        @Query("access_key") access_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ) : Response<ApiResponse>

    @Headers("apikey: ${EndPoints.API_KEY}")
    @GET("/fixer/{date}")
    suspend fun convertBaseCurrency(
        @Path("date") date: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ) : Response<BaseRatesResponse>


}