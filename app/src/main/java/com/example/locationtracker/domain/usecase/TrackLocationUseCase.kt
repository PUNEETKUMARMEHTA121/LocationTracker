package com.example.locationtracker.domain.usecase

import com.example.locationtracker.domain.model.LocationModel
import com.example.locationtracker.domain.repository.LocationRepository

class TrackLocationUseCase(private val locationRepository: LocationRepository) {

    suspend fun execute(location: LocationModel) {
        locationRepository.saveLocation(location)
    }
}