package com.example.nutrilens.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.nutrilens.data.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievement")
    fun getAchievements(): Flow<List<AchievementEntity>>

    @Query("UPDATE achievement SET isUnlocked = 1 WHERE id = :id")
    suspend fun unlock(id: Long)
}
