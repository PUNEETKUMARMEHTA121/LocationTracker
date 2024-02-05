package com.example.locationtracker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.locationtracker.data.model.LocationData

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(locationData: LocationData)

    @Query("SELECT * FROM location_data ORDER BY timestamp DESC")
    suspend fun getLocationHistory(): List<LocationData>
}