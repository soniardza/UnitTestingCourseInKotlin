package com.example.testDrivenDevelopment.example11

import com.example.testDrivenDevelopment.example11.cart.CartItem
import com.example.testDrivenDevelopment.example11.networking.CartItemSchema
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.Callback
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.FailReason.NETWORK_ERROR
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchCartItemsManualTestDoublesUseCaseTest {
    // region helper fields ------------------------------------------------------------------------
    private lateinit var getCartItemsHttpEndpointTd: GetCartItemsHttpEndpointTd

    @Mock
    lateinit var listenerMock1: FetchCartItemsUseCase.Listener

    @Mock
    lateinit var listenerMock2: FetchCartItemsUseCase.Listener

    @Captor
    val acListCartItem: ArgumentCaptor<List<CartItem?>> =
        ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<CartItem?>>

    object MockitoHelper {
        // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    }
    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var systemUnderTest: FetchCartItemsUseCase

    @Before
    fun setup() {
        getCartItemsHttpEndpointTd = GetCartItemsHttpEndpointTd()
        systemUnderTest = FetchCartItemsUseCase(getCartItemsHttpEndpointTd)
        success()
    }

    private fun getCartItemSchemes(): List<CartItemSchema> {
        val schemas = ArrayList<CartItemSchema>()
        schemas.add(CartItemSchema(ID, TITLE, DESCRIPTION, PRICE))
        return schemas
    }

    @Test
    fun fetchCartItems_correctLimitPassedToEndpoint() {
        // Arrange
        // Act
        systemUnderTest.fetchCartItemsAndNotify(LIMIT)
        // Assert
        assertEquals(1, getCartItemsHttpEndpointTd.invocationCount)
        assertEquals(LIMIT, getCartItemsHttpEndpointTd.lastLimit)
    }

    @Test
    fun fetchCartItems_success_observersNotifiedWithCorrectData() {
        // Arrange
        // Act
        systemUnderTest.registerListener(listenerMock1)
        systemUnderTest.registerListener(listenerMock2)
        systemUnderTest.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onCartItemsFetched(MockitoHelper.capture(acListCartItem))
        verify(listenerMock2).onCartItemsFetched(MockitoHelper.capture(acListCartItem))
        val captures = acListCartItem.allValues
        val capture1 = captures[0]
        val capture2 = captures[1]
        assertEquals(getCartItems(), capture1)
        assertEquals(getCartItems(), capture2)
    }

    @Test
    fun fetchCartItems_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        systemUnderTest.registerListener(listenerMock1)
        systemUnderTest.registerListener(listenerMock2)
        systemUnderTest.unregisterListener(listenerMock2)
        systemUnderTest.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onCartItemsFetched(Mockito.any() as List<CartItem?>?)
        Mockito.verifyNoMoreInteractions(listenerMock2)
    }

    @Test
    fun fetchCartItems_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError()
        // Act
        systemUnderTest.registerListener(listenerMock1)
        systemUnderTest.registerListener(listenerMock2)
        systemUnderTest.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onFetchCartItemsFailed()
        verify(listenerMock2).onFetchCartItemsFailed()
    }

    @Test
    fun fetchCartItems_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError()
        // Act
        systemUnderTest.registerListener(listenerMock1)
        systemUnderTest.registerListener(listenerMock2)
        systemUnderTest.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(listenerMock1).onFetchCartItemsFailed()
        verify(listenerMock2).onFetchCartItemsFailed()
    }

    // region helper methods -----------------------------------------------------------------------
    private fun getCartItems(): List<CartItem> {
        val cartItems = ArrayList<CartItem>()
        cartItems.add(CartItem(ID, TITLE, DESCRIPTION, PRICE))
        return cartItems
    }

    private fun success() {
        // no-op
    }

    private fun networkError() {
        getCartItemsHttpEndpointTd.networkError = true
    }

    private fun generalError() {
        getCartItemsHttpEndpointTd.generalError = true
    }

    // endregion helper methods --------------------------------------------------------------------
    // region helper classes -----------------------------------------------------------------------
    inner class GetCartItemsHttpEndpointTd : GetCartItemsHttpEndpoint {
        var invocationCount = 0
        var lastLimit = 0
        var networkError = false
        var generalError = false

        override fun getCartItems(
            limit: Int,
            callback: Callback,
        ) {
            invocationCount++
            lastLimit = limit
            if (networkError) {
                callback.onGetCartItemsFailed(NETWORK_ERROR)
            } else if (generalError) {
                callback.onGetCartItemsFailed(GENERAL_ERROR)
            } else {
                callback.onGetCartItemsSucceeded(getCartItemSchemes())
            }
        }
    } // endregion helper classes ------------------------------------------------------------------

    companion object {
        // region constants ------------------------------------------------------------------------
        const val LIMIT = 10
        const val PRICE = 5
        const val DESCRIPTION = "description"
        const val TITLE = "title"
        const val ID = "id"
        // endregion constants ---------------------------------------------------------------------
    }
}
