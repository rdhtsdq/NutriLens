package com.example.nutrilens.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nutrilens.data.dao.AchievementDao
import com.example.nutrilens.data.dao.FoodLogDao
import com.example.nutrilens.data.dao.UserProfileDao
import com.example.nutrilens.data.entity.AchievementEntity
import com.example.nutrilens.data.entity.FoodLogEntity
import com.example.nutrilens.data.entity.UserProfileEntity

@Database(
    entities = [UserProfileEntity::class, FoodLogEntity::class, AchievementEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FoodRiskDatabase : RoomDatabase() {

    abstract fun foodLogDao(): FoodLogDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: FoodRiskDatabase? = null

        fun getInstance(context: Context): FoodRiskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodRiskDatabase::class.java,
                    "food_risk_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
