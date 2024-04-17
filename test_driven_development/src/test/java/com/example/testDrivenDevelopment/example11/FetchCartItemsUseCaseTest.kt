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
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchCartItemsUseCaseTest {
    // region helper fields ------------------------------------------------------------------------
    @Mock
    lateinit var getCartItemsHttpEndpointMock: GetCartItemsHttpEndpoint

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

        fun <T> anyObject(): T {
            any<T>()
            return uninitialized()
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> uninitialized(): T = null as T
    }

    // endregion helper fields ---------------------------------------------------------------------
    private lateinit var systemUnderTest: FetchCartItemsUseCase

    @Before
    fun setup() {
        systemUnderTest = FetchCartItemsUseCase(getCartItemsHttpEndpointMock)
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
        val captor: ArgumentCaptor<Int> = ArgumentCaptor.forClass(Int::class.java)
        // Act
        systemUnderTest.fetchCartItemsAndNotify(LIMIT)
        // Assert
        verify(getCartItemsHttpEndpointMock).getCartItems(
            MockitoHelper.capture(captor),
            MockitoHelper.anyObject(),
        )
        assertEquals(LIMIT, captor.value)
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
        verify(listenerMock1).onCartItemsFetched(any() as List<CartItem?>?)
        verifyNoMoreInteractions(listenerMock2)
    }

    @Test
    fun fetchCartItems_generalError_observersNotifiedOfFailure() {
        // Arrange
        generaError()
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
        doAnswer { invocation: InvocationOnMock ->
            val args = invocation.arguments
            val callback = args[1] as Callback
            callback.onGetCartItemsSucceeded(getCartItemSchemes())
            null
        }.`when`(getCartItemsHttpEndpointMock).getCartItems(anyInt(), MockitoHelper.anyObject())
    }

    private fun networkError() {
        doAnswer { invocation: InvocationOnMock ->
            val args = invocation.arguments
            val callback = args[1] as Callback
            callback.onGetCartItemsFailed(NETWORK_ERROR)
            null
        }.`when`(getCartItemsHttpEndpointMock).getCartItems(anyInt(), MockitoHelper.anyObject())
    }

    private fun generaError() {
        doAnswer { invocation: InvocationOnMock ->
            val args = invocation.arguments
            val callback = args[1] as Callback
            callback.onGetCartItemsFailed(GENERAL_ERROR)
            null
        }.`when`(getCartItemsHttpEndpointMock).getCartItems(anyInt(), MockitoHelper.anyObject())
    }

    // endregion helper methods --------------------------------------------------------------------

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
