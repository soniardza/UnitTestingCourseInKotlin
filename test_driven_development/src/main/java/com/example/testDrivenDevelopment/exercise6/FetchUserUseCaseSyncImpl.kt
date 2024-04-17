package com.example.testDrivenDevelopment.exercise6

import com.example.testDrivenDevelopment.exercise6.FetchUserUseCaseSync.Status
import com.example.testDrivenDevelopment.exercise6.FetchUserUseCaseSync.UseCaseResult
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.SUCCESS
import com.example.testDrivenDevelopment.exercise6.networking.NetworkErrorException
import com.example.testDrivenDevelopment.exercise6.users.User
import com.example.testDrivenDevelopment.exercise6.users.UsersCache

class FetchUserUseCaseSyncImpl(
    private val fetchUserHttpEndpointSync: FetchUserHttpEndpointSync,
    private val usersCache: UsersCache,
) : FetchUserUseCaseSync {
    override fun fetchUserSync(userId: String?): UseCaseResult? {
        val cachedUser = usersCache.getUser(userId)
        if (cachedUser != null) {
            return UseCaseResult(Status.SUCCESS, cachedUser)
        }

        try {
            val endpointResult = fetchUserHttpEndpointSync.fetchUserSync(userId)
            if (endpointResult != null) {
                return when (endpointResult.status) {
                    SUCCESS -> {
                        val user = User(endpointResult.userId, endpointResult.username)
                        usersCache.cacheUser(user)
                        UseCaseResult(Status.SUCCESS, user)
                    }

                    AUTH_ERROR -> UseCaseResult(Status.FAILURE, null)
                    GENERAL_ERROR -> UseCaseResult(Status.FAILURE, null)
                }
            }
        } catch (e: NetworkErrorException) {
            return UseCaseResult(Status.NETWORK_ERROR, null)
        }

        return UseCaseResult(Status.FAILURE, null)
    }
}
