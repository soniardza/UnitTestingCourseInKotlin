package com.example.testDrivenDevelopment.exercise6.users

import androidx.annotation.Nullable

interface UsersCache {
    fun cacheUser(user: User?)

    @Nullable
    fun getUser(userId: String?): User?
}
