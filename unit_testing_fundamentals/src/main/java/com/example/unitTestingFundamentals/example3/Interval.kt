package com.example.unitTestingFundamentals.example3

class Interval(start: Int, end: Int) {
    val start: Int
    val end: Int

    init {
        require(start < end) { "invalid interval range" }
        this.start = start
        this.end = end
    }
}

