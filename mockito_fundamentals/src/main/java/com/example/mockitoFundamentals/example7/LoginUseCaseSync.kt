package com.example.mockitoFundamentals.example7

import com.example.mockitoFundamentals.example7.authtoken.AuthTokenCache
import com.example.mockitoFundamentals.example7.eventbus.EventBusPoster
import com.example.mockitoFundamentals.example7.eventbus.LoggedInEvent
import com.example.mockitoFundamentals.example7.networking.LoginHttpEndpointSync
import com.example.mockitoFundamentals.example7.networking.NetworkErrorException

class LoginUseCaseSync(
    loginHttpEndpointSync: LoginHttpEndpointSync?,
    authTokenCache: AuthTokenCache?,
    eventBusPoster: EventBusPoster?,
) {

    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    private var mLoginHttpEndpointSync: LoginHttpEndpointSync? = loginHttpEndpointSync
    private var mAuthTokenCache: AuthTokenCache? = authTokenCache
    private var mEventBusPoster: EventBusPoster? = eventBusPoster

    fun loginSync(username: String?, password: String?): UseCaseResult? {
        val endpointEndpointResult: LoginHttpEndpointSync.EndpointResult? = try {
            mLoginHttpEndpointSync?.loginSync(username, password)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }
        return if (isSuccessfulEndpointResult(endpointEndpointResult)) {
            mAuthTokenCache?.cacheAuthToken(endpointEndpointResult?.authToken)
            mEventBusPoster?.postEvent(LoggedInEvent())
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: LoginHttpEndpointSync.EndpointResult?): Boolean {
        return endpointResult?.status === LoginHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}
