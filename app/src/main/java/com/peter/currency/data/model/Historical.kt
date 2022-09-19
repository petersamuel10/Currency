package com.peter.currency.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Historical(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fromCurrency: String,
    val ToCurrency: String,
    val amount: String,
    val result: String,
    val date: String
)