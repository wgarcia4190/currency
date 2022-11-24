package com.example.currencyapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(currencyEntity: CurrencyEntity)

    @Query("SELECT * FROM CurrencyEntity")
    fun getAllDataSet(): LiveData<List<CurrencyEntity>>
}