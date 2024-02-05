package com.example.locationtracker.data.local.repository

import com.example.locationtracker.data.local.database.dao.LocationDao
import com.example.locationtracker.data.model.LocationData
import com.example.locationtracker.domain.model.LocationModel
import com.example.locationtracker.domain.repository.LocationRepository

class LocationRepositoryImpl(private val locationDao: LocationDao) : LocationRepository {

    override suspend fun saveLocation(location: LocationModel) {
        val locationData = LocationData(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = location.timestamp
        )
        locationDao.insertLocation(locationData)
    }

    override suspend fun getLocationHistory(): List<LocationModel> {
        val locationDataList = locationDao.getLocationHistory()
        return locationDataList.map {
            LocationModel(
                latitude = it.latitude,
                longitude = it.longitude,
                timestamp = it.timestamp
            )
        }
    }
}
