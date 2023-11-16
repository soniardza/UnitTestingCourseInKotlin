package com.example.testDoublesFundamentals.example5

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class UserInputValidatorTest {
    var SUT: UserInputValidator? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = UserInputValidator()
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isValidFullName_validFullName_trueReturned() {
        val result = SUT!!.isValidFullName("validFullName")
        assertThat(result, `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isValidFullName_invalidFullName_falseReturned() {
        val result = SUT!!.isValidFullName("")
        assertThat(result, `is`(false))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isValidUsername_validUsername_trueReturned() {
        val result = SUT!!.isValidUsername("validUsername")
        assertThat(result, `is`(true))
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun isValidUsername_invalidUsername_falseReturned() {
        val result = SUT!!.isValidUsername("")
        assertThat(result, `is`(false))
    }
}
