package com.example.nutrilens.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_log")
data class FoodLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodName: String,
    val calories: Int,
    val sugarContent: Double,
    val riskLevel: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String? = null
)
