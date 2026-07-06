package com.example.nutrilens.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrilens.data.database.FoodRiskDatabase
import com.example.nutrilens.data.entity.FoodLogEntity
import com.example.nutrilens.data.repository.FoodRepository
import com.example.nutrilens.data.repository.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepository = FoodRepository(
        FoodRiskDatabase.getInstance(application).foodLogDao()
    )

    private val profileRepository = ProfileRepository(
        FoodRiskDatabase.getInstance(application).userProfileDao()
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

    val targetCalories: StateFlow<Int> = profileRepository
        .observeProfile()
        .map { it?.targetCalories ?: 2000 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2000)

    val todayFoods: StateFlow<List<FoodLogEntity>> = foodRepository
        .getTodayFoods(todayStart, todayEnd)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCalories: StateFlow<Int> = foodRepository
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
            foodRepository.deleteFood(foodLog)
        }
    }
}
