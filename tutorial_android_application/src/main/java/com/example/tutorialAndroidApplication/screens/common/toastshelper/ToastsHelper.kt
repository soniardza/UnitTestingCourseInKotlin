package com.example.tutorialAndroidApplication.screens.common.toastshelper

import android.content.Context
import android.widget.Toast
import com.example.tutorial_android_application.R

class ToastsHelper(private val context: Context) {
    fun showUseCaseError() {
        Toast.makeText(context, R.string.error_network_call_failed, Toast.LENGTH_SHORT).show()
    }
}
