package com.example.unitTestingFundamentals.exercise3

import com.example.unitTestingFundamentals.example3.Interval
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class IntervalsAdjacencyDetectorTest {
    var SUT: IntervalsAdjacencyDetector? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = IntervalsAdjacencyDetector()
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1BeforeInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(8, 12)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1BeforeAndAdjacentToInterval2_trueReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 12)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1OverlapsInterval2OnStart_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(3, 12)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1ContainedWithinInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 12)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1ContainsInterval2_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(0, 3)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1EqualsInterval2_falseReturned() { // failed
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-1, 5)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1OverlapsInterval2OnEnd_falseReturned() {
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-4, 4)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1AfterAndAdjacentToInterval2_trueReturned() {
        val interval1 = Interval(12, 15)
        val interval2 = Interval(5, 12)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isAdjacent_interval1AfterInterval2_falseReturned() { // failed
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-10, -3)
        val result = SUT!!.isAdjacent(interval1, interval2)
        assertThat(result, `is`(false))
    }
}
