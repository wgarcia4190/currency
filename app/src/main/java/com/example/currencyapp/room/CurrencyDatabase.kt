package com.example.currencyapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDAO
}

private lateinit var INSTANCE: CurrencyDatabase

fun getDatabase(context: Context): CurrencyDatabase {

    synchronized(CurrencyDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                CurrencyDatabase::class.java,
                "database"
            ).build()
        }
    }

    return INSTANCE
}