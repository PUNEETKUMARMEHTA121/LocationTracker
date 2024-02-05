package com.example.locationtracker.domain.usecase

import com.example.locationtracker.domain.model.LocationModel
import com.example.locationtracker.domain.repository.LocationRepository

class GetLocationHistoryUseCase(private val locationRepository: LocationRepository) {

    suspend fun execute(): List<LocationModel> {
        return locationRepository.getLocationHistory()
    }
}