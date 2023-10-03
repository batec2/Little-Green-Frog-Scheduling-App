package com.example.f23hopper

import android.app.Application
import com.example.f23hopper.data.AppContainer
import com.example.f23hopper.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SchedulerApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}