package com.example.unitTestingInAndroid.example14

import android.app.Activity

class MyActivity : Activity() {
    var count = 0
        private set

    override fun onStart() {
        super.onStart()
        count++
    }
}
