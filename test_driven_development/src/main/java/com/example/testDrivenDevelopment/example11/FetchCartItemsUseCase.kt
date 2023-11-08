package com.example.testDrivenDevelopment.example11

import com.example.testDrivenDevelopment.example11.cart.CartItem
import com.example.testDrivenDevelopment.example11.networking.CartItemSchema
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint
import com.example.testDrivenDevelopment.example11.networking.GetCartItemsHttpEndpoint.FailReason

class FetchCartItemsUseCase(private val mGetCartItemsHttpEndpoint: GetCartItemsHttpEndpoint) {
    interface Listener {
        fun onCartItemsFetched(capture: List<CartItem?>?)
        fun onFetchCartItemsFailed()
    }

    private val mListeners: MutableList<Listener> = ArrayList()
    fun fetchCartItemsAndNotify(limit: Int) {
        mGetCartItemsHttpEndpoint.getCartItems(
            limit,
            object : GetCartItemsHttpEndpoint.Callback {
                override fun onGetCartItemsSucceeded(cartItems: List<CartItemSchema?>?) {
                    notifySucceeded(cartItems)
                }

                override fun onGetCartItemsFailed(failReason: FailReason?) {
                    when (failReason) {
                        FailReason.GENERAL_ERROR, FailReason.NETWORK_ERROR -> notifyFailed()
                        else -> {}
                    }
                }
            },
        )
    }

    private fun notifySucceeded(cartItems: List<CartItemSchema?>?) {
        for (listener in mListeners) {
            listener.onCartItemsFetched(cartItemsFromSchemas(cartItems))
        }
    }

    private fun notifyFailed() {
        for (listener in mListeners) {
            listener.onFetchCartItemsFailed()
        }
    }

    private fun cartItemsFromSchemas(cartItemSchemas: List<CartItemSchema?>?): List<CartItem?> {
        val cartItems: MutableList<CartItem?> = ArrayList()
        for (schema in cartItemSchemas!!) {
            cartItems.add(
                CartItem(
                    schema!!.id,
                    schema.title,
                    schema.description,
                    schema.price,
                ),
            )
        }
        return cartItems
    }

    fun registerListener(listener: Listener) {
        mListeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        mListeners.remove(listener)
    }
}
