package com.example.locationtracker.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.locationtracker.R
import com.example.locationtracker.databinding.FragmentHistoryBinding
import com.example.locationtracker.presentation.main.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var polyline: Polyline? = null
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history, container, false
        )

        // Initialize the map fragment
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        (requireActivity() as MainActivity).viewModel.mPastLocations.observe(viewLifecycleOwner) {
            updateMapWithPolyLine(it)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (requireActivity() as MainActivity).viewModel.getLocationHistory()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
//        updateMapWithPolyLine()
    }

    private fun updateMapWithPolyLine(latLngList: List<LatLng>) {
        val polylineOptions = PolylineOptions()
            .addAll(latLngList)
//            .add(LatLng(26.9124, 75.7873))
//            .add(LatLng(24.5854, 73.7125))
//            .add(LatLng(26.2389, 73.0243))
//            .add(LatLng(26.4499, 74.6399))
            //            .add(LatLng(37.7749, -122.4194))
            //            .add(LatLng(34.0522, -118.2437))
            //            .add(LatLng(40.7128, -74.0060))
            .color(ContextCompat.getColor(requireContext(), R.color.polyline_color))

        polyline = map.addPolyline(polylineOptions)

        // Calculate bounds based on the LatLng coordinates of the polyline
        polyline?.let { line ->
            // Calculate bounds based on the LatLng coordinates of the polyline
            val coordinates: List<LatLng> = line.points
            val builder = LatLngBounds.Builder()
            for (latLng in coordinates) {
                builder.include(latLng)
            }

            // Get the LatLngBounds object
            val bounds = builder.build()

            // Move the camera to show the entire polyline with padding
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::map.isInitialized) {
            map.clear()
        }
    }
}
