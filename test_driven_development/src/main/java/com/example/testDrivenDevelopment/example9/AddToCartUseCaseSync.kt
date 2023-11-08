package com.example.testDrivenDevelopment.example9

import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync
import com.example.testDrivenDevelopment.example9.networking.CartItemScheme
import com.example.testDrivenDevelopment.example9.networking.NetworkErrorException

class AddToCartUseCaseSync(private val mAddToCartHttpEndpointSync: AddToCartHttpEndpointSync) {
    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    fun addToCartSync(offerId: String?, amount: Int): UseCaseResult {
        val result: AddToCartHttpEndpointSync.EndpointResult? = try {
            mAddToCartHttpEndpointSync.addToCartSync(CartItemScheme(offerId!!, amount))
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }
        return when (result) {
            AddToCartHttpEndpointSync.EndpointResult.SUCCESS -> UseCaseResult.SUCCESS
            AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR, AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR -> UseCaseResult.FAILURE
            else -> throw RuntimeException("invalid endpoint result: $result")
        }
    }
}
