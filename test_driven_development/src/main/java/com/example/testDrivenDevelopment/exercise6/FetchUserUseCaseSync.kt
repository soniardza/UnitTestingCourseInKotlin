package com.example.testDrivenDevelopment.exercise6

import com.example.testDrivenDevelopment.exercise6.User.User

internal interface FetchUserUseCaseSync {
    enum class Status {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    class UseCaseResult(
        val status: Status,
        var user: User,
    ) {

        init {
            user = user
        }
    }

    fun fetchUserSync(userId: String?): UseCaseResult?
}
