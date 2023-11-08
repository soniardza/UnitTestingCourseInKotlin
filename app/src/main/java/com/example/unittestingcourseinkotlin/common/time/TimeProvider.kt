package com.example.unittestingcourseinkotlin.common.time

class TimeProvider {
    val currentTimestamp: Long
        get() = System.currentTimeMillis()
}
