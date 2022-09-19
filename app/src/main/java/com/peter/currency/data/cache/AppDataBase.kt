package com.peter.currency.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peter.currency.data.model.Historical

@Database(
    entities = [Historical::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {
        val DATABASE_NAME: String = "currency_db"
    }
}