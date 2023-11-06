package com.example.unit_testing_fundamentals.exercise3

import com.example.unit_testing_fundamentals.example3.Interval

class IntervalsAdjacencyDetector {
    /**
     * @return true if the intervals are adjacent, but don't overlap
     */
    fun isAdjacent(interval1: Interval, interval2: Interval): Boolean {
        // this implementation contains two bugs:
        // 1. will erroneously report adjacent if interval1 and interval2 are the same
        // 2. will erroneously report adjacent if interval1 after interval2
        return interval1.getEnd() == interval2.getStart() || interval1.getStart() >= interval2.getEnd() || isSameIntervals(
            interval1,
            interval2,
        )
    }

    private fun isSameIntervals(interval1: Interval, interval2: Interval): Boolean {
        return interval1.getStart() == interval2.getStart() && interval1.getEnd() == interval2.getEnd()
    }
}
