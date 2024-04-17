package com.example.testDrivenDevelopment.example10

import com.example.testDrivenDevelopment.example10.PingServerSyncUseCase.UseCaseResult.FAILURE
import com.example.testDrivenDevelopment.example10.PingServerSyncUseCase.UseCaseResult.SUCCESS
import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync
import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync.EndpointResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PingServerSyncUseCaseTest {
    // region helper fields ------------------------------------------------------------------------
    @Mock
    private lateinit var pingServerHttpEndpointSyncMock: PingServerHttpEndpointSync

    // endregion helper fields ---------------------------------------------------------------------
    private lateinit var systemUnderTest: PingServerSyncUseCase

    @Before
    fun setup() {
        systemUnderTest = PingServerSyncUseCase(pingServerHttpEndpointSyncMock)
        success()
    }

    @Test
    @Throws(Exception::class)
    fun pingServerSync_success_successReturned() {
        // Arrange
        // Act
        val result = systemUnderTest.pingServerSync()
        // Assert
        assertEquals(SUCCESS, result)
    }

    @Test
    fun pingServerSync_generalError_failureReturned() {
        // Arrange
        generalError()
        // Act
        val result = systemUnderTest.pingServerSync()
        // Assert
        assertEquals(FAILURE, result)
    }

    @Test
    fun pingServerSync_networkError_failureReturned() {
        // Arrange
        networkError()
        // Act
        val result = systemUnderTest.pingServerSync()
        // Assert
        assertEquals(FAILURE, result)
    }

    // region helper methods -----------------------------------------------------------------------
    private fun success() {
        `when`(
            pingServerHttpEndpointSyncMock.pingServerSync(),
        ).thenReturn(EndpointResult.SUCCESS)
    }

    private fun networkError() {
        `when`(
            pingServerHttpEndpointSyncMock.pingServerSync(),
        ).thenReturn(EndpointResult.NETWORK_ERROR)
    }

    private fun generalError() {
        `when`(
            pingServerHttpEndpointSyncMock.pingServerSync(),
        ).thenReturn(EndpointResult.GENERAL_ERROR)
    }
    // endregion helper methods ------------------------------------------------------------------
}
