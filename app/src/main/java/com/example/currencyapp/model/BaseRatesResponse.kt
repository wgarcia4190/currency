package com.example.currencyapp.model

data class BaseRatesResponse (
    val success: Boolean,
    val timestamp: Long,
    val historical: Boolean,
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val error: Error
)
