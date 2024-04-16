package com.example.mockitoFundamentals.example7

import com.example.mockitoFundamentals.example7.authtoken.AuthTokenCache
import com.example.mockitoFundamentals.example7.eventbus.EventBusPoster
import com.example.mockitoFundamentals.example7.eventbus.LoggedInEvent
import com.example.mockitoFundamentals.example7.networking.LoginHttpEndpointSync
import com.example.mockitoFundamentals.example7.networking.NetworkErrorException

class LoginUseCaseSync(
    private val loginHttpEndpointSync: LoginHttpEndpointSync,
    private val authTokenCache: AuthTokenCache,
    private val eventBusPoster: EventBusPoster
) {

    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun loginSync(username: String, password: String): UseCaseResult {
        val endpointResult: LoginHttpEndpointSync.EndpointResult
        try {
            endpointResult = loginHttpEndpointSync.loginSync(username, password)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return if (isSuccessfulEndpointResult(endpointResult)) {
            authTokenCache.cacheAuthToken(endpointResult.authToken)
            eventBusPoster.postEvent(LoggedInEvent())
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: LoginHttpEndpointSync.EndpointResult): Boolean {
        return endpointResult.status == LoginHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}
