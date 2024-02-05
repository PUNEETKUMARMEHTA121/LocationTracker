package com.example.locationtracker.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.locationtracker.data.local.database.dao.LocationDao
import com.example.locationtracker.data.model.LocationData

@Database(entities = [LocationData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "your_database_name"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun createLocationDao(context: Context): LocationDao {
            val appDatabase = getDatabase(context)
            return appDatabase.locationDao()
        }
    }
}