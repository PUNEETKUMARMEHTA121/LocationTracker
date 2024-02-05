package com.example.locationtracker.domain.repository

import com.example.locationtracker.domain.model.LocationModel

interface LocationRepository {
    suspend fun saveLocation(location: LocationModel)
    suspend fun getLocationHistory(): List<LocationModel>
}