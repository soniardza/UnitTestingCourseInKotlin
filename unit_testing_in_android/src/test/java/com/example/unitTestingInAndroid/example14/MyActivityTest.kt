package com.example.unitTestingInAndroid.example14

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MyActivityTest {
    private lateinit var onStartListener: OnStartListener
    private lateinit var systemUnderTest: MyActivity

    @Before
    fun setup() {
        onStartListener = mock(OnStartListener::class.java)
        systemUnderTest = MyActivity(onStartListener)
    }

    @Test
    fun onStart_incrementsCountByOne() {
        // Arrange
        // Act
        systemUnderTest.onStart()
        val result = systemUnderTest.getCount()
        // Assert
        assertEquals(1, result)
    }
}
