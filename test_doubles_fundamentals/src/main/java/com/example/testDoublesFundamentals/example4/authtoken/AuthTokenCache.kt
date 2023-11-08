package com.example.testDoublesFundamentals.example4.authtoken

interface AuthTokenCache {
    fun cacheAuthToken(authToken: String?)

    fun getAuthToken(): String?
}
