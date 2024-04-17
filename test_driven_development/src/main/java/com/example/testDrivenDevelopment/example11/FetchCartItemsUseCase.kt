package com.example.testDrivenDevelopment.example11

import com.example.testDrivenDevelopment.example11.cart.CartItem
import com.example.testDrivenDevelopment.example11.networking.CartItemSchema
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.FailReason
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.FailReason.NETWORK_ERROR

class FetchCartItemsUseCase(private val getCartItemsHttpEndpoint: GetCartItemsHttpEndpoint) {
    interface Listener {
        fun onCartItemsFetched(capture: List<CartItem?>?)

        fun onFetchCartItemsFailed()
    }

    private val listeners = mutableListOf<Listener>()

    fun fetchCartItemsAndNotify(limit: Int) {
        getCartItemsHttpEndpoint.getCartItems(
            limit,
            object : GetCartItemsHttpEndpoint.Callback {
                override fun onGetCartItemsSucceeded(cartItems: List<CartItemSchema?>?) {
                    notifySucceeded(cartItems)
                }

                override fun onGetCartItemsFailed(failReason: FailReason?) {
                    when (failReason) {
                        GENERAL_ERROR, NETWORK_ERROR -> notifyFailed()
                        else -> throw RuntimeException("invalid endpoint result: $failReason")
                    }
                }
            },
        )
    }

    private fun notifySucceeded(cartItems: List<CartItemSchema?>?) {
        for (listener in listeners) {
            listener.onCartItemsFetched(cartItemsFromSchemas(cartItems))
        }
    }

    private fun notifyFailed() {
        for (listener in listeners) {
            listener.onFetchCartItemsFailed()
        }
    }

    private fun cartItemsFromSchemas(cartItemSchemas: List<CartItemSchema?>?): List<CartItem?> {
        val cartItems = mutableListOf<CartItem>()
        if (cartItemSchemas != null) {
            for (schema in cartItemSchemas) {
                if (schema != null) {
                    cartItems.add(
                        CartItem(
                            schema.id,
                            schema.title,
                            schema.description,
                            schema.price,
                        ),
                    )
                }
            }
        }
        return cartItems
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }
}
