package com.example.testDoublesFundamentals.example6

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class FitnessTrackerTest {
    private var SUT: FitnessTracker? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = FitnessTracker()
    }

    @Test
    @Throws(Exception::class)
    fun step_totalIncremented() {
        SUT!!.step()
        assertThat(SUT!!.totalSteps, `is`(1))
    }

    @Test
    @Throws(Exception::class)
    fun runStep_totalIncrementedByCorrectRatio() {
        SUT!!.runStep()
        assertThat(SUT!!.totalSteps, `is`(2))
    }
}
