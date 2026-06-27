package com.example.nutrilens.ui.manualinput

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrilens.data.database.FoodRiskDatabase
import com.example.nutrilens.data.entity.FoodLogEntity
import com.example.nutrilens.data.repository.FoodRepository
import kotlinx.coroutines.launch

class ManualInputViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodRepository(
        FoodRiskDatabase.getInstance(application).foodLogDao()
    )

    fun saveFood(foodName: String, calories: Int, sugar: Double, riskLevel: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.saveFood(
                FoodLogEntity(
                    foodName = foodName,
                    calories = calories,
                    sugarContent = sugar,
                    riskLevel = riskLevel
                )
            )
            onSaved()
        }
    }
}
