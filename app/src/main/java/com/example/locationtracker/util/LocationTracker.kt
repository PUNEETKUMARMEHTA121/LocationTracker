package com.example.locationtracker.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationTracker(private val context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    suspend fun startTracking(
        interval: Long,
        locationCallback: (Location) -> Unit,
        onPermissionDenied: () -> Unit,
        onGpsDisabled: () -> Unit
    ) {
        if (!isGpsEnabled()) {
            onGpsDisabled.invoke()
        } else if (!hasLocationPermission()) {
            onPermissionDenied.invoke()
        } else {
            internalStartTracking(interval, locationCallback)
        }
    }

    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    fun isGpsEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun internalStartTracking(
        interval: Long,
        locationCallback: (Location) -> Unit
    ) {
        val locationRequest = LocationRequest.create()
            .setInterval(interval)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        this.locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    locationCallback.invoke(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            this.locationCallback as LocationCallback,
            null
        )

    }


    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
