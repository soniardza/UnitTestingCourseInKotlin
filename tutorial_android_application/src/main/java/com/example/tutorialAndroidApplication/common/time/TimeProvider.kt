package com.example.tutorialAndroidApplication.common.time

class TimeProvider {
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}
