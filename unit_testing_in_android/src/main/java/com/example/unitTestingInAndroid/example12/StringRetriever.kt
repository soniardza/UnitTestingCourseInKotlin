package com.example.unitTestingInAndroid.example12

import android.content.Context

class StringRetriever(private val context: Context) {
    fun getString(id: Int): String {
        return context.getString(id)
    }
}
