package com.peter.currency.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peter.currency.data.model.Historical

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConvertOp(historicalData: Historical)

    @Query("SELECT * FROM historical")
    suspend fun getHistorical(): List<Historical>
}
