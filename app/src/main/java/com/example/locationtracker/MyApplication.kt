package com.example.locationtracker

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.locationtracker.util.notification.AppLifecycleObserver

class MyApplication : Application() {

    var isTrackingEnabled: Boolean = false
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))
    }
}
