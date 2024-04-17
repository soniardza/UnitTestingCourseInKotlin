package com.example.unitTestingInAndroid.example13

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AndroidUnitTestingProblemsTest {

    private lateinit var systemUnderTest: AndroidUnitTestingProblems

    @Before
    fun setup() {
        systemUnderTest = AndroidUnitTestingProblems()
    }

    @Test
    fun testStaticApiCall() {
        // Arrange
        // Act
        systemUnderTest.callStaticAndroidApi("")
        // Assert
        assertTrue(true)
    }
}
