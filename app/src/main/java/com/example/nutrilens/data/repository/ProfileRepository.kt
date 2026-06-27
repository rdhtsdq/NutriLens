package com.example.nutrilens.data.repository

import com.example.nutrilens.data.dao.UserProfileDao
import com.example.nutrilens.data.entity.UserProfileEntity

class ProfileRepository(private val userProfileDao: UserProfileDao) {

    suspend fun getProfile(): UserProfileEntity? = userProfileDao.getProfile()

    suspend fun saveProfile(profile: UserProfileEntity) = userProfileDao.saveProfile(profile)
}
