package com.example.tutorialAndroidApplication.screens.common.controllers

interface BackPressDispatcher {
    fun registerListener(listener: BackPressedListener)

    fun unregisterListener(listener: BackPressedListener)
}
