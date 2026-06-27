package com.example.nutrilens.data.repository

import com.example.nutrilens.data.dao.FoodLogDao
import com.example.nutrilens.data.entity.FoodLogEntity
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodLogDao: FoodLogDao) {

    suspend fun saveFood(foodLog: FoodLogEntity) = foodLogDao.insert(foodLog)

    suspend fun updateFood(foodLog: FoodLogEntity) = foodLogDao.update(foodLog)

    suspend fun deleteFood(foodLog: FoodLogEntity) = foodLogDao.delete(foodLog)

    fun getTodayFoods(startOfDay: Long, endOfDay: Long): Flow<List<FoodLogEntity>> =
        foodLogDao.getTodayFoods(startOfDay, endOfDay)

    fun getWeeklyFoods(startOfWeek: Long): Flow<List<FoodLogEntity>> =
        foodLogDao.getWeeklyFoods(startOfWeek)

    fun getTotalCaloriesToday(startOfDay: Long, endOfDay: Long): Flow<Int?> =
        foodLogDao.getTotalCaloriesToday(startOfDay, endOfDay)
}
