package com.example.nutrilens.domain.model

data class FoodRecognitionResult(
    val foodName: String,
    val confidence: Float,
    val isRecognized: Boolean
)
