package com.example.locationtracker.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.locationtracker.domain.usecase.GetLocationHistoryUseCase
import com.example.locationtracker.domain.usecase.TrackLocationUseCase

class MainViewModelFactory(
    private val trackLocationUseCase: TrackLocationUseCase,
    private val getLocationHistoryUseCase: GetLocationHistoryUseCase,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(trackLocationUseCase, getLocationHistoryUseCase, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
