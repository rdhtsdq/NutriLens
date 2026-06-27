package com.example.nutrilens.domain.model

data class FoodLog(
    val id: Long = 0,
    val foodName: String,
    val calories: Int,
    val sugarContent: Double,
    val riskLevel: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String? = null
)
