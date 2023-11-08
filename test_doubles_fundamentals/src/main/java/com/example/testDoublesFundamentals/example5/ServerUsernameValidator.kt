package com.example.testDoublesFundamentals.example5

class ServerUsernameValidator {
    fun isValidUsername(username: String?): Boolean {
        // this sleep mimics network request that checks whether username is free, but fails due to
        // absence of network connection
        return try {
            Thread.sleep(1000)
            throw RuntimeException("no network connection")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}
