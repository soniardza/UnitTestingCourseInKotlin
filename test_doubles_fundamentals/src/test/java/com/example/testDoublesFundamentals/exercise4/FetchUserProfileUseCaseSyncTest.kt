package com.example.testDoublesFundamentals.exercise4

import com.example.testDoublesFundamentals.example4.networking.NetworkErrorException
import com.example.testDoublesFundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult
import com.example.testDoublesFundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.example.testDoublesFundamentals.exercise4.users.User
import com.example.testDoublesFundamentals.exercise4.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FetchUserProfileUseCaseSyncTest {
    private lateinit var userProfileHttpEndpointSyncTd: UserProfileHttpEndpointSyncTd
    private lateinit var usersCacheTd: UsersCacheTd
    private lateinit var systemUnderTest: FetchUserProfileUseCaseSync

    @Before
    fun setup() {
        userProfileHttpEndpointSyncTd = UserProfileHttpEndpointSyncTd()
        usersCacheTd = UsersCacheTd()
        systemUnderTest = FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd)
    }

    @Test
    fun fetchUserProfileSync_success_userIdPassedToEndpoint() {
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assertEquals(USER_ID, userProfileHttpEndpointSyncTd.userId)
    }

    @Test
    fun fetchUserProfileSync_success_userCached() {
        systemUnderTest.fetchUserProfileSync(USER_ID)
        val cachedUser = usersCacheTd.getUser(USER_ID)
        assertNotNull(cachedUser)
        assertEquals(USER_ID, cachedUser?.userId)
        assertEquals(FULL_NAME, cachedUser?.fullName)
        assertEquals(IMAGE_URL, cachedUser?.imageUrl)
    }

    @Test
    fun fetchUserProfileSync_generalError_userNotCached() {
        userProfileHttpEndpointSyncTd.isGeneralError = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assertNull(usersCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfileSync_authError_userNotCached() {
        userProfileHttpEndpointSyncTd.isAuthError = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assertNull(usersCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfileSync_serverError_userNotCached() {
        userProfileHttpEndpointSyncTd.isServerError = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assertNull(usersCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfileSync_success_successReturned() {
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.SUCCESS, result)
    }

    @Test
    fun fetchUserProfileSync_serverError_failureReturned() {
        userProfileHttpEndpointSyncTd.isServerError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun fetchUserProfileSync_authError_failureReturned() {
        userProfileHttpEndpointSyncTd.isAuthError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun fetchUserProfileSync_generalError_failureReturned() {
        userProfileHttpEndpointSyncTd.isGeneralError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun fetchUserProfileSync_networkError_networkErrorReturned() {
        userProfileHttpEndpointSyncTd.isNetworkError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.NETWORK_ERROR, result)
    }

    // ---------------------------------------------------------------------------------------------
    // Helper classes
    private inner class UserProfileHttpEndpointSyncTd : UserProfileHttpEndpointSync {
        var userId = ""
        var isGeneralError: Boolean = false
        var isAuthError: Boolean = false
        var isServerError: Boolean = false
        var isNetworkError: Boolean = false

        override fun getUserProfile(userId: String): UserProfileHttpEndpointSync.EndpointResult {
            this.userId = userId
            return when {
                isGeneralError -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                    "",
                    "",
                    ""
                )
                isAuthError -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                    "",
                    "",
                    ""
                )
                isServerError -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                    "",
                    "",
                    ""
                )
                isNetworkError -> throw NetworkErrorException()
                else -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    USER_ID,
                    FULL_NAME,
                    IMAGE_URL
                )
            }
        }
    }

    private inner class UsersCacheTd : UsersCache {
        private val users: MutableList<User> = ArrayList()

        override fun cacheUser(user: User) {
            val existingUser = user.userId?.let { getUser(it) }
            if (existingUser != null) {
                users.remove(existingUser)
            }
            users.add(user)
        }

        override fun getUser(userId: String): User? {
            for (user in users) {
                if (user.userId == userId) {
                    return user
                }
            }
            return null
        }
    }

    companion object {
        const val USER_ID = "userId"
        const val FULL_NAME = "fullName"
        const val IMAGE_URL = "imageUrl"
    }
}
