package com.example.locationtracker.presentation.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.locationtracker.R
import com.example.locationtracker.data.local.database.AppDatabase
import com.example.locationtracker.data.local.database.dao.LocationDao
import com.example.locationtracker.data.local.repository.LocationRepositoryImpl
import com.example.locationtracker.databinding.ActivityMainBinding
import com.example.locationtracker.domain.usecase.GetLocationHistoryUseCase
import com.example.locationtracker.domain.usecase.TrackLocationUseCase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO Remove , use d.i
        val locationDao: LocationDao = AppDatabase.createLocationDao(this)
        val locationRepo = LocationRepositoryImpl(locationDao)
        val trackLocationUseCase = TrackLocationUseCase(locationRepo)
        val getLocationHistoryUseCase = GetLocationHistoryUseCase(locationRepo)

        val mainViewModelFactory = MainViewModelFactory(
            trackLocationUseCase,
            getLocationHistoryUseCase,
            this
        )
        viewModel = ViewModelProvider(
            this,
            mainViewModelFactory
        )[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(binding.container.id, MainFragment())
//                .commitNow()
//        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.selectedItemId = R.id.menu_current_location

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_current_location -> {
                    // Handle current location selection
                    navController.navigate(R.id.mainFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.menu_history -> {
                    // Handle history selection
                    navController.navigate(R.id.historyFragment)
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false
            }
        }
    }
}
