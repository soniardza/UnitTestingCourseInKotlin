package com.example.unittestingcourseinkotlin.common

import android.app.Application
import com.example.unittestingcourseinkotlin.common.dependencyinjection.CompositionRoot

class CustomApplication : Application() {
    var compositionRoot: CompositionRoot? = null
        private set

    override fun onCreate() {
        super.onCreate()
        compositionRoot = CompositionRoot()
    }
}
