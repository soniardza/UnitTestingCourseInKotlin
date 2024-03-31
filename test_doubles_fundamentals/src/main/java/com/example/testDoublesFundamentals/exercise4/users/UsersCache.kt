package com.example.testDoublesFundamentals.exercise4.users

import androidx.annotation.Nullable

interface UsersCache {
    fun cacheUser(user: User)

    @Nullable
    fun getUser(userId: String): User?
}
