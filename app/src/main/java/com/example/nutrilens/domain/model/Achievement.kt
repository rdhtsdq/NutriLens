package com.example.nutrilens.domain.model

data class Achievement(
    val id: Long = 0,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false
)
