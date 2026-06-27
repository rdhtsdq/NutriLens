package com.example.nutrilens.data.repository

import com.example.nutrilens.data.dao.AchievementDao
import com.example.nutrilens.data.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

class AchievementRepository(private val achievementDao: AchievementDao) {

    fun getAchievements(): Flow<List<AchievementEntity>> = achievementDao.getAchievements()

    suspend fun unlock(id: Long) = achievementDao.unlock(id)
}
