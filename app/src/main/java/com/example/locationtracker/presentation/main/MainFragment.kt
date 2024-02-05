package com.example.locationtracker.presentation.main

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.example.locationtracker.databinding.FragmentMainBinding
import com.example.locationtracker.domain.model.LocationModel
import com.example.locationtracker.util.Constants
import com.example.locationtracker.util.IViewClicked
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.startTracking = object : IViewClicked {
            override fun onclick() {
                showIntervalPickerDialog()
//                setTrackingVisibility(false)
//                (requireActivity() as MainActivity).viewModel.startTracking(randomInterval, {
//                    showPermissionDeniedDialog()
//                }, { showGpsNotEnabledDialog() })
            }
        }

        binding.stopTracking = object : IViewClicked {
            override fun onclick() {
                setTrackingVisibility(true)
                (requireActivity() as MainActivity).viewModel.stopTracking()
            }
        }

        binding.mapView.getMapAsync { googleMap ->
            (requireActivity() as MainActivity).viewModel.locationLiveData.observe(
                viewLifecycleOwner
            ) { location ->
                // Update the map with the current location
                updateMap(googleMap, location)
            }
        }
        setTrackingVisibility(true)
    }

    private fun updateMap(googleMap: GoogleMap, location: LocationModel) {
        val latLng = LatLng(location.latitude, location.longitude)
        // Update the camera position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        // Add a marker for the current location
        googleMap.clear() // Clear existing markers
        googleMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
    }

    private fun showPermissionDeniedDialog() {
        setTrackingVisibility(true)
        AlertDialog.Builder(requireContext())
            //TODO Remove hard -coding
            .setTitle("Permission Denied")
            .setMessage("Location permission is required for tracking. Please enable it in the app settings.")
            .setPositiveButton("OK") { _, _ ->
                // Open app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setTrackingVisibility(visibility: Boolean) {
        binding.trackingVisibility = visibility
    }

    private fun showGpsNotEnabledDialog() {
        setTrackingVisibility(true)
        AlertDialog.Builder(requireContext())
            // TODO: Remove hard-coding
            .setTitle("GPS Not Enabled")
            .setMessage("GPS is not enabled on your device. Please enable GPS in your device settings.")
            .setPositiveButton("OK") { _, _ ->
                // Open device settings to enable GPS
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showIntervalPickerDialog() {
        val numberPicker = NumberPicker(requireContext())
        numberPicker.minValue = Constants.minIntervalMinutes
        numberPicker.maxValue = Constants.maxIntervalMinutes
        numberPicker.value = Constants.defaultIntervalMinutes

        AlertDialog.Builder(requireContext())
            //TOdo take value from String
            .setTitle("Select Tracking Interval")
            .setView(numberPicker)
            //TOdo take value from String
            .setPositiveButton("Start Tracking") { _, _ ->
                val selectedInterval =
                    numberPicker.value * 60000L // Convert minutes to milliseconds
                setTrackingVisibility(false)
                (requireActivity() as MainActivity).viewModel.startTracking(
                    selectedInterval,
                    {
                        showPermissionDeniedDialog()
                    },
                    {
                        showGpsNotEnabledDialog()
                    }
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}