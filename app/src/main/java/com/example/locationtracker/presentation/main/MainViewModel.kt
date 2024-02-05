package com.example.locationtracker.presentation.main

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationtracker.domain.model.LocationModel
import com.example.locationtracker.domain.usecase.GetLocationHistoryUseCase
import com.example.locationtracker.domain.usecase.TrackLocationUseCase
import com.example.locationtracker.util.LocationTracker
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val trackLocationUseCase: TrackLocationUseCase,
    private val getLocationHistoryUseCase: GetLocationHistoryUseCase,
    context: Context
) : ViewModel() {
    //TODO Remove, use D.i
    private val locationTracker: LocationTracker = LocationTracker(context)

    private val _locationLiveData = MutableLiveData<LocationModel>()
    val locationLiveData: LiveData<LocationModel> get() = _locationLiveData

    private val _mPastLocations = MutableLiveData<List<LatLng>>()
    val mPastLocations : LiveData<List<LatLng>> get() = _mPastLocations

    // Track location
    fun startTracking(interval: Long, onPermissionDenied: () -> Unit, onGpsDisabled: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            locationTracker.startTracking(
                interval,
                ::onLocationUpdate,
                onPermissionDenied,
                onGpsDisabled
            )
        }
    }

    private fun onLocationUpdate(location: Location) {
        val localModel = LocationModel(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = location.time
        )
        _locationLiveData.postValue(localModel)
        //TODO Update here visibility of tracking
        viewModelScope.launch(Dispatchers.Main) {
            trackLocationUseCase.execute(localModel)
        }
    }

    fun stopTracking() {
        viewModelScope.launch(Dispatchers.Main) {
            locationTracker.stopTracking()
        }
    }

    // Function to get location history
    fun getLocationHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            getLocationHistoryUseCase.execute().let { list ->
                if (list.isNotEmpty()) {
                    _mPastLocations.postValue(list.map {
                        LatLng(it.latitude, it.longitude)
                    })
                }
            }
        }
    }
}
