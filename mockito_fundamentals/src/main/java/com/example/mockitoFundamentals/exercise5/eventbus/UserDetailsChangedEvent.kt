package com.example.mockitoFundamentals.exercise5.eventbus

import com.example.mockitoFundamentals.exercise5.users.User

class UserDetailsChangedEvent(
    private val user: User
)

