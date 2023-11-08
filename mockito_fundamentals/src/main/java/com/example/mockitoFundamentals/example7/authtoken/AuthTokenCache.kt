package com.example.mockitoFundamentals.example7.authtoken

interface AuthTokenCache {
    fun cacheAuthToken(authToken: String?)

    fun getAuthToken(): String?
}
