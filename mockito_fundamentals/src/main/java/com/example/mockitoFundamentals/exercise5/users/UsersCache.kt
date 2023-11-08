package com.example.mockitoFundamentals.exercise5.users

import androidx.annotation.Nullable

interface UsersCache {
    fun cacheUser(user: User?)

    @Nullable
    fun getUser(userId: String?): User?
}
