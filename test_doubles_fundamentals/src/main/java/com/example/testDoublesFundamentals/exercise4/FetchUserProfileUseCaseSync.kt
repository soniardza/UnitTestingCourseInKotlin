package com.example.testDoublesFundamentals.exercise4

import com.example.testDoublesFundamentals.example4.networking.NetworkErrorException
import com.example.testDoublesFundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.example.testDoublesFundamentals.exercise4.users.User
import com.example.testDoublesFundamentals.exercise4.users.UsersCache

class FetchUserProfileUseCaseSync(
    private val mUserProfileHttpEndpointSync: UserProfileHttpEndpointSync? = null,
    private val mUsersCache: UsersCache? = null,
) {
    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    private val mEndpointResult: UserProfileHttpEndpointSync.EndpointResult? = null

    fun fetchUserProfileSync(userId: String?): UseCaseResult {
        val endpointResult: UserProfileHttpEndpointSync.EndpointResult?
        try {
            // the bug here is that userId is not passed to endpoint
            endpointResult = mUserProfileHttpEndpointSync?.getUserProfile("")
            // the bug here is that I don't check for successful result and it's also a duplication
            // of the call later in this method
            mUsersCache?.cacheUser(
                User(userId, mEndpointResult?.fullName, mEndpointResult?.imageUrl),
            )
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }
        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache?.cacheUser(
                User(userId, mEndpointResult?.fullName, mEndpointResult?.imageUrl),
            )
        }

        // the bug here is that I return wrong result in case of an unsuccessful server response
        return UseCaseResult.SUCCESS
    }

    private fun isSuccessfulEndpointResult(endpointResult: UserProfileHttpEndpointSync.EndpointResult?): Boolean {
        return endpointResult?.status === UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}
