package com.example.mockitoFundamentals.exercise5

import com.example.mockitoFundamentals.exercise5.eventbus.EventBusPoster
import com.example.mockitoFundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.example.mockitoFundamentals.exercise5.networking.NetworkErrorException
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus
import com.example.mockitoFundamentals.exercise5.users.User
import com.example.mockitoFundamentals.exercise5.users.UsersCache

class UpdateUsernameUseCaseSync(
    private val updateUsernameHttpEndpointSync: UpdateUsernameHttpEndpointSync,
    private val usersCache: UsersCache,
    private val eventBusPoster: EventBusPoster,
) {
    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }


    fun updateUsernameSync(userId: String?, username: String?): UseCaseResult {
        val endpointResult: EndpointResult?
        try {
            endpointResult = updateUsernameHttpEndpointSync.updateUsername(userId, username)
        } catch (e: NetworkErrorException) {
            // the bug here was "swallowed" exception instead of return
            return UseCaseResult.NETWORK_ERROR
        }
        return if (isSuccessfulEndpointResult(endpointResult)) {
            // the bug here was reversed arguments
            val user = endpointResult?.let { User(it.userId, it.username) }
            eventBusPoster.postEvent(UserDetailsChangedEvent(User(userId, username)))
            usersCache.cacheUser(user)
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: EndpointResult?): Boolean {
        // the bug here was the wrong definition of successful response
        return endpointResult?.status == EndpointResultStatus.SUCCESS
    }
}
