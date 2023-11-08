package com.example.testDoublesFundamentals.example4

import com.example.testDoublesFundamentals.example4.authtoken.AuthTokenCache
import com.example.testDoublesFundamentals.example4.eventbus.EventBusPoster
import com.example.testDoublesFundamentals.example4.eventbus.LoggedInEvent
import com.example.testDoublesFundamentals.example4.networking.LoginHttpEndpointSync
import com.example.testDoublesFundamentals.example4.networking.LoginHttpEndpointSync.EndpointResult
import com.example.testDoublesFundamentals.example4.networking.NetworkErrorException

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
        val endpointEndpointResult: EndpointResult? = try {
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

    private fun isSuccessfulEndpointResult(endpointResult: EndpointResult?): Boolean {
        return endpointResult?.status === LoginHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}
