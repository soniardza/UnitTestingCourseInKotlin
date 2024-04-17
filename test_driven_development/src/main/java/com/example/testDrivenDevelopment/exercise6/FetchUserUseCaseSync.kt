package com.example.testDrivenDevelopment.exercise6

import com.example.testDrivenDevelopment.exercise6.users.User

interface FetchUserUseCaseSync {
    enum class Status {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR,
    }

    data class UseCaseResult(
        val status: Status,
        val user: User?,
    )

    fun fetchUserSync(userId: String?): UseCaseResult?
}
