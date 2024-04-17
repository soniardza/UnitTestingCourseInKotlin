package com.example.testDrivenDevelopment.example9

import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR
import com.example.testDrivenDevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.SUCCESS
import com.example.testDrivenDevelopment.example9.networking.CartItemScheme
import com.example.testDrivenDevelopment.example9.networking.NetworkErrorException

class AddToCartUseCaseSync(private val addToCartHttpEndpointSync: AddToCartHttpEndpointSync) {
    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR,
    }

    fun addToCartSync(
        offerId: String,
        amount: Int,
    ): UseCaseResult {
        val result: EndpointResult?

        try {
            result = addToCartHttpEndpointSync.addToCartSync(CartItemScheme(offerId, amount))
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return when (result) {
            SUCCESS -> UseCaseResult.SUCCESS
            AUTH_ERROR, GENERAL_ERROR -> UseCaseResult.FAILURE
            else -> throw RuntimeException("invalid endpoint result: $result")
        }
    }
}
