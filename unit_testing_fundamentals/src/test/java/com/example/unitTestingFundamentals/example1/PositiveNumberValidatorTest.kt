package com.example.unitTestingFundamentals.example1

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PositiveNumberValidatorTest {
    var SUT: PositiveNumberValidator? = null

    @Before
    fun setup() {
        SUT = PositiveNumberValidator()
    }

    @Test
    fun test1() {
        val result = SUT!!.isPositive(-1)
        Assert.assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    fun test2() {
        val result = SUT!!.isPositive(0)
        Assert.assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    fun test3() {
        val result = SUT!!.isPositive(1)
        Assert.assertThat(result, CoreMatchers.`is`(true))
    }
}
