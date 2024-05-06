package com.example.tutorialAndroidApplication.common

import android.app.Application
import com.example.tutorialAndroidApplication.common.dependencyinjection.CompositionRoot

class CustomApplication : Application() {
    val compositionRoot: CompositionRoot by lazy {
        CompositionRoot()
    }

    override fun onCreate() {
        super.onCreate()
    }
}
