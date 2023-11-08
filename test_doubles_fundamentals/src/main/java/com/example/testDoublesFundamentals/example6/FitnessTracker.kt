package com.example.testDoublesFundamentals.example6

class FitnessTracker {
    private val counter: Counter? = null

    fun step() {
        counter?.add()
    }

    fun runStep() {
        counter?.add(RUN_STEPS_FACTOR)
    }

    val totalSteps: Int
        get() = counter!!.total

    companion object {
        const val RUN_STEPS_FACTOR = 2
    }
}
