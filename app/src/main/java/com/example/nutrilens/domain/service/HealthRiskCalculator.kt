package com.example.nutrilens.domain.service

class HealthRiskCalculator {

    fun calculate(calories: Int, sugar: Double): String {
        val highCalorie = calories > 400
        val highSugar = sugar > 20.0
        val mediumCalorie = calories in 201..400
        val mediumSugar = sugar in 10.1..20.0

        return when {
            highCalorie || highSugar -> "HIGH"
            mediumCalorie || mediumSugar -> "MEDIUM"
            else -> "LOW"
        }
    }
}
