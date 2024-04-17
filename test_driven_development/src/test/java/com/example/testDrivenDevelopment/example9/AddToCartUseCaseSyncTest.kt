package com.example.testDrivenDevelopment.example9

import com.example.testDrivenDevelopment.example9.AddToCartUseCaseSync.UseCaseResult
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.SUCCESS
import com.example.testDrivenDevelopment.example9.networking.CartItemScheme
import com.example.testDrivenDevelopment.example9.networking.NetworkErrorException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddToCartUseCaseSyncTest {
    // region helper fields ------------------------------------------------------------------------
    @Mock
    private lateinit var addToCartHttpEndpointSyncMock: AddToCartHttpEndpointSync

    object MockitoHelper {
        // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    }
    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var systemUnderTest: AddToCartUseCaseSync

    @Before
    fun setup() {
        systemUnderTest = AddToCartUseCaseSync(addToCartHttpEndpointSyncMock)
        success()
    }

    @Test
    fun addToCartSync_correctParametersPassedToEndpoint() {
        // Arrange
        val captor: ArgumentCaptor<CartItemScheme> =
            ArgumentCaptor.forClass(CartItemScheme::class.java)
        // Act
        systemUnderTest.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        verify(addToCartHttpEndpointSyncMock).addToCartSync(MockitoHelper.capture(captor))
        assertEquals(OFFER_ID, captor.value.offerId)
        assertEquals(AMOUNT, captor.value.amount)
    }

    @Test
    fun addToCartSync_success_successReturned() {
        // Arrange
        // Act
        val result: UseCaseResult = systemUnderTest.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(UseCaseResult.SUCCESS, result)
    }

    @Test
    fun addToCartSync_authError_failureReturned() {
        // Arrange
        authError()
        // Act
        val result: UseCaseResult = systemUnderTest.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun addToCartSync_generalError_failureReturned() {
        // Arrange
        generalError()
        // Act
        val result: UseCaseResult = systemUnderTest.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun addToCartSync_networkError_networkErrorReturned() {
        // Arrange
        networkError()
        // Act
        val result: UseCaseResult = systemUnderTest.addToCartSync(OFFER_ID, AMOUNT)
        // Assert
        assertEquals(UseCaseResult.NETWORK_ERROR, result)
    }

    // region helper methods -----------------------------------------------------------------------
    private fun success() {
        `when`(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme::class.java))).thenReturn(
            SUCCESS,
        )
    }

    private fun authError() {
        `when`(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme::class.java))).thenReturn(
            AUTH_ERROR,
        )
    }

    private fun generalError() {
        `when`(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme::class.java))).thenReturn(
            GENERAL_ERROR,
        )
    }

    private fun networkError() {
        `when`(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme::class.java))).thenThrow(
            NetworkErrorException(),
        )
    }

    // endregion helper methods ------------------------------------------------------------------

    companion object {
        // region constants ------------------------------------------------------------------------
        const val OFFER_ID = "offerId"
        const val AMOUNT = 4
        // endregion constants ---------------------------------------------------------------------
    }
}
