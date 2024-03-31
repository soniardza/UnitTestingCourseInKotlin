package com.example.testDoublesFundamentals.exercise4

import com.example.testDoublesFundamentals.example4.networking.NetworkErrorException
import com.example.testDoublesFundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.example.testDoublesFundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResult
import com.example.testDoublesFundamentals.exercise4.users.User
import com.example.testDoublesFundamentals.exercise4.users.UsersCache

class FetchUserProfileUseCaseSync(
    private val userProfileHttpEndpointSync: UserProfileHttpEndpointSync,
    private val usersCache: UsersCache
) {
    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    fun fetchUserProfileSync(userId: String): UseCaseResult {
        val endpointResult: EndpointResult
        try {
            endpointResult = userProfileHttpEndpointSync.getUserProfile(userId)
            if (isSuccessfulEndpointResult(endpointResult)) {
                usersCache.cacheUser(
                    User(
                        userId,
                        endpointResult.fullName,
                        endpointResult.imageUrl
                    )
                )
            }
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        if (!isSuccessfulEndpointResult(endpointResult)) {
            return UseCaseResult.FAILURE
        }

        return UseCaseResult.SUCCESS
    }

    private fun isSuccessfulEndpointResult(endpointResult: EndpointResult): Boolean {
        return endpointResult.status === UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}
