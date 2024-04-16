package com.example.mockitoFundamentals.example8

class UserMess(
    val fullName: String,
    val address: Address,
    val phoneNumber: PhoneNumber
) {

    fun logOut() {
        // real implementation here
    }

    fun connectWith(otherUser: UserMess?) {
        // real implementation here
    }

    // real implementation here
    val connectedUsers: List<UserMess>?
        get() = // real implementation here
            null

    fun disconnectFromAll() {
        // real implementation here
    }
}
