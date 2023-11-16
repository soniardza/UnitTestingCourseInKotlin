package com.example.unitTestingFundamentals.exercise2

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class StringDuplicatorTest {
    private var SUT: StringDuplicator? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = StringDuplicator()
    }

    @Test
    @Throws(Exception::class)
    fun duplicate_emptyString_emptyStringReturned() {
        val result = SUT!!.duplicate("")
        assertThat(result, `is`(""))
    }

    @Test
    @Throws(Exception::class)
    fun duplicate_singleCharacter_duplicatedStringReturned() {
        val result = SUT!!.duplicate("a")
        assertThat(result, `is`("aa"))
    }

    @Test
    @Throws(Exception::class)
    fun duplicate_longString_duplicatedStringReturned() {
        val result = SUT!!.duplicate("Sonia Rodriguez")
        assertThat(result, `is`("Sonia RodriguezSonia Rodriguez"))
    }
}
