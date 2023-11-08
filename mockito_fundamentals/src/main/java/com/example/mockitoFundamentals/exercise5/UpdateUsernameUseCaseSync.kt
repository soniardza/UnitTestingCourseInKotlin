package com.example.mockitoFundamentals.exercise5

import com.example.mockitoFundamentals.exercise5.eventbus.EventBusPoster
import com.example.mockitoFundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.example.mockitoFundamentals.exercise5.networking.NetworkErrorException
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.example.mockitoFundamentals.exercise5.users.User
import com.example.mockitoFundamentals.exercise5.users.UsersCache

class UpdateUsernameUseCaseSync(
    private val mUpdateUsernameHttpEndpointSync: UpdateUsernameHttpEndpointSync,
    private val mUsersCache: UsersCache,
    eventBusPoster: EventBusPoster,
) {
    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    private val mEventBusPoster: EventBusPoster

    init {
        mEventBusPoster = eventBusPoster
    }

    fun updateUsernameSync(userId: String?, username: String?): UseCaseResult {
        var endpointResult: UpdateUsernameHttpEndpointSync.EndpointResult? = null
        try {
            endpointResult = mUpdateUsernameHttpEndpointSync.updateUsername(userId, username)
        } catch (e: NetworkErrorException) {
            // the bug here is "swallowed" exception instead of return
        }
        return if (isSuccessfulEndpointResult(endpointResult)) {
            // the bug here is reversed arguments
            val user = endpointResult?.let { User(it.username, endpointResult.userId) }
            mEventBusPoster.postEvent(UserDetailsChangedEvent(User(userId, username)))
            mUsersCache.cacheUser(user)
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: UpdateUsernameHttpEndpointSync.EndpointResult?): Boolean {
        // the bug here is the wrong definition of successful response
        return (
            endpointResult?.status === UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS ||
                endpointResult?.status === UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR
            )
    }
}
