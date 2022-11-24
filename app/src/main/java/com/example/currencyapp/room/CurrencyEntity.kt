package com.example.currencyapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val Id: Int? = null,
    val date: String,
    val rate: Double?= null ,
    val timestamp: Long? = null,
    val amount: Long? = null,
    val from: String,
    val to: String,
    val result: Double,
)