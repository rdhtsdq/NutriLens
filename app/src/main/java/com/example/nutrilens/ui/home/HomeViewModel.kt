package com.example.nutrilens.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrilens.data.database.FoodRiskDatabase
import com.example.nutrilens.data.entity.FoodLogEntity
import com.example.nutrilens.data.repository.FoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodRepository(
        FoodRiskDatabase.getInstance(application).foodLogDao()
    )

    private val todayStart: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }

    private val todayEnd: Long
        get() = todayStart + 24 * 60 * 60 * 1000

    val todayFoods: StateFlow<List<FoodLogEntity>> = repository
        .getTodayFoods(todayStart, todayEnd)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCalories: StateFlow<Int> = repository
        .getTotalCaloriesToday(todayStart, todayEnd)
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val greeting: String
        get() {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return when (hour) {
                in 0..11 -> "Good morning"
                in 12..16 -> "Good afternoon"
                else -> "Good evening"
            }
        }

    fun deleteFood(foodLog: FoodLogEntity) {
        viewModelScope.launch {
            repository.deleteFood(foodLog)
        }
    }
}
