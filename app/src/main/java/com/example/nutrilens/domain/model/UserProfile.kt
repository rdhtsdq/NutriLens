package com.example.nutrilens.domain.model

data class UserProfile(
    val id: Int = 1,
    val name: String = "",
    val age: Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f
)
