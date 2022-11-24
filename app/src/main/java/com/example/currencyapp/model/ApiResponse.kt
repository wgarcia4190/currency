package com.example.currencyapp.model

data class ApiResponse(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean,
    val error: Error
)

data class Info(
    val rate: Double,
    val timestamp: Long
)

data class Query(
    val amount: Long,
    val from: String,
    val to: String
)

data class Error (
    val code: Long,
    val type: String,
    val info: String
)