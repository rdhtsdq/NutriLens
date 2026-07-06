package com.example.nutrilens.data.repository

import com.example.nutrilens.data.dao.UserProfileDao
import com.example.nutrilens.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val userProfileDao: UserProfileDao) {

    fun observeProfile(): Flow<UserProfileEntity?> = userProfileDao.observeProfile()

    suspend fun saveProfile(profile: UserProfileEntity) = userProfileDao.saveProfile(profile)
}
