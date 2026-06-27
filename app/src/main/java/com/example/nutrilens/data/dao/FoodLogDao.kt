package com.example.nutrilens.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutrilens.data.entity.FoodLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodLog: FoodLogEntity)

    @Update
    suspend fun update(foodLog: FoodLogEntity)

    @Delete
    suspend fun delete(foodLog: FoodLogEntity)

    @Query("SELECT * FROM food_log WHERE timestamp >= :startOfDay AND timestamp < :endOfDay ORDER BY timestamp DESC")
    fun getTodayFoods(startOfDay: Long, endOfDay: Long): Flow<List<FoodLogEntity>>

    @Query("SELECT * FROM food_log WHERE timestamp >= :startOfWeek ORDER BY timestamp DESC")
    fun getWeeklyFoods(startOfWeek: Long): Flow<List<FoodLogEntity>>

    @Query("SELECT SUM(calories) FROM food_log WHERE timestamp >= :startOfDay AND timestamp < :endOfDay")
    fun getTotalCaloriesToday(startOfDay: Long, endOfDay: Long): Flow<Int?>
}
