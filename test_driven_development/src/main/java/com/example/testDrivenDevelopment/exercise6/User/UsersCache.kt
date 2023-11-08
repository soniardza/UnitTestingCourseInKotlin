package com.example.testDrivenDevelopment.exercise6.User

import androidx.annotation.Nullable

interface UsersCache {
    fun cacheUser(user: User?)

    @Nullable
    fun getUser(userId: String?): User?
}
