package com.example.testDrivenDevelopment.example11.networking

interface GetCartItemsHttpEndpoint {
    enum class FailReason {
        GENERAL_ERROR, NETWORK_ERROR
    }

    interface Callback {
        fun onGetCartItemsSucceeded(cartItems: List<CartItemSchema?>?)
        fun onGetCartItemsFailed(failReason: FailReason?)
    }

    /**
     * @param limit max amount of cart items to fetch
     * @param callback object to be notified when the request completes
     */
    fun getCartItems(limit: Int, callback: Callback?)
}

