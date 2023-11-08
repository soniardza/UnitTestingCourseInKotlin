package com.example.unitTestingFundamentals.example3

class Interval {
    private var mStart = 0
    private var mEnd = 0

    fun Interval(start: Int, end: Int) {
        require(start < end) { "invalid interval range" }
        mStart = start
        mEnd = end
    }

    fun getStart(): Int {
        return mStart
    }

    fun getEnd(): Int {
        return mEnd
    }
}
