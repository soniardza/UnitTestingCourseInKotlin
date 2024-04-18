package com.example.unitTestingInAndroid.example14

class MyActivity(private val onStartListener: OnStartListener) {
    private var mCount: Int = 0

    fun onStart() {
        onStartListener.onStart()
        mCount++
    }

    fun getCount(): Int {
        return mCount
    }
}

interface OnStartListener {
    fun onStart()
}
