package com.example.mockitoFundamentals.exercise5.eventbus

import com.example.mockitoFundamentals.exercise5.users.User

class UserDetailsChangedEvent(user: User) {
    private val mUser: User

    init {
        mUser = user
    }

    val user: User
        get() = mUser
}

