package com.example.testDrivenDevelopment.exercise6.User

class User(val userId: String, val username: String) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as User
        return if (userId != user.userId) false else username == user.username
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}
