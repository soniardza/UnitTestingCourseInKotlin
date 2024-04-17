package com.example.unitTestingInAndroid.example12

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StringRetrieverTest {
    // region constants ----------------------------------------------------------------------------
    companion object {
        const val ID = 10
        const val STRING = "string"
    }
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    lateinit var contextMock: Context
    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var systemUnderTest: StringRetriever

    @Before
    fun setup() {
        systemUnderTest = StringRetriever(contextMock)
    }

    @Test
    fun getString_correctParameterPassedToContext() {
        // Arrange
        `when`(contextMock.getString(ID)).thenReturn(STRING)
        // Act
        systemUnderTest.getString(ID)
        // Assert
        verify(contextMock).getString(ID)
    }

    @Test
    fun getString_correctResultReturned() {
        // Arrange
        `when`(contextMock.getString(ID)).thenReturn(STRING)
        // Act
        val result = systemUnderTest.getString(ID)
        // Assert
        assertEquals(STRING, result)
    }
}
