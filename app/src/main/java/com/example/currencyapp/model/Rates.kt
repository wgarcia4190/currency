package com.example.currencyapp.model

data class Rates(
    val Id: Int,
    val date: String,
    val rate: Double,
    val timestamp: Long,
    val amount: Long,
    val from: String,
    val to: String,
    val result: Double,
)
