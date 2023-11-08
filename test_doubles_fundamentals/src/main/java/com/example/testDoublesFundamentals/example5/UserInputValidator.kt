package com.example.testDoublesFundamentals.example5

class UserInputValidator {
    private var fullNameValidator: FullNameValidator? = null
    private var serverUsernameValidator: ServerUsernameValidator? = null

    fun isValidFullName(fullName: String?): Boolean {
        return fullName?.let { fullNameValidator?.isValidFullName(it) } ?: false
    }

    fun isValidUsername(username: String?): Boolean {
        return serverUsernameValidator?.isValidUsername(username) ?: false
    }
}
