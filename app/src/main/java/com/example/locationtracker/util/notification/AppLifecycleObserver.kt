package com.example.locationtracker.util.notification

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.locationtracker.MyApplication

class AppLifecycleObserver(private val application: Application) : LifecycleObserver {
    private val context: Context = application.applicationContext

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        // App comes to the foreground
        // You can cancel the notification or perform any other actions
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        // App goes to the background
        // Show the notification when tracking is active
        if (isTrackingLocation()) {
            NotificationHelper(context).showTrackingNotification()
        }
    }

    private fun isTrackingLocation(): Boolean {
        val myApplication = context.applicationContext as MyApplication
        return myApplication.isTrackingEnabled
    }
}
