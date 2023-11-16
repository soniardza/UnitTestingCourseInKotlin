package com.example.testDoublesFundamentals.exercise4

import com.example.testDoublesFundamentals.example4.networking.NetworkErrorException
import com.example.testDoublesFundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult
import com.example.testDoublesFundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.example.testDoublesFundamentals.exercise4.users.User
import com.example.testDoublesFundamentals.exercise4.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.jetbrains.annotations.Nullable
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class FetchUserProfileUseCaseSyncTest {
    private var mUserProfileHttpEndpointSyncTd: UserProfileHttpEndpointSyncTd? = null
    private var mUsersCacheTd: UsersCacheTd? = null
    private var SUT: FetchUserProfileUseCaseSync? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        mUserProfileHttpEndpointSyncTd = UserProfileHttpEndpointSyncTd()
        mUsersCacheTd = UsersCacheTd()
        SUT = FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncTd, mUsersCacheTd)
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_success_userIdPassedToEndpoint() {
        SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(mUserProfileHttpEndpointSyncTd!!.mUserId, `is`(USER_ID))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_success_userCached() {
        SUT!!.fetchUserProfileSync(USER_ID)
        val cachedUser = mUsersCacheTd!!.getUser(USER_ID)
        assertThat(cachedUser!!.userId, `is`(USER_ID))
        assertThat(cachedUser!!.fullName, `is`(FULL_NAME))
        assertThat(cachedUser!!.imageUrl, `is`(IMAGE_URL))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_generalError_userNotCached() {
        mUserProfileHttpEndpointSyncTd!!.mIsGeneralError = true
        SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(mUsersCacheTd!!.getUser(USER_ID), `is`(nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_authError_userNotCached() {
        mUserProfileHttpEndpointSyncTd!!.mIsAuthError = true
        SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(mUsersCacheTd!!.getUser(USER_ID), `is`(nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_serverError_userNotCached() {
        mUserProfileHttpEndpointSyncTd!!.mIsServerError = true
        SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(mUsersCacheTd!!.getUser(USER_ID), `is`(nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_success_successReturned() {
        val result: UseCaseResult = SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(result, `is`(UseCaseResult.SUCCESS))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_serverError_failureReturned() {
        mUserProfileHttpEndpointSyncTd!!.mIsServerError = true
        val result: UseCaseResult = SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_authError_failureReturned() {
        mUserProfileHttpEndpointSyncTd!!.mIsAuthError = true
        val result: UseCaseResult = SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_generalError_failureReturned() {
        mUserProfileHttpEndpointSyncTd!!.mIsGeneralError = true
        val result: UseCaseResult = SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_networkError_networkErrorReturned() {
        mUserProfileHttpEndpointSyncTd!!.mIsNetworkError = true
        val result: UseCaseResult = SUT!!.fetchUserProfileSync(USER_ID)
        assertThat(result, `is`(UseCaseResult.NETWORK_ERROR))
    }

    // ---------------------------------------------------------------------------------------------
    // Helper classes
    class UserProfileHttpEndpointSyncTd : UserProfileHttpEndpointSync {
        var mUserId: String? = ""
        var mIsGeneralError = false
        var mIsAuthError = false
        var mIsServerError = false
        var mIsNetworkError = false

        @Throws(NetworkErrorException::class)
        override fun getUserProfile(userId: String?): UserProfileHttpEndpointSync.EndpointResult? {
            mUserId = userId
            return if (mIsGeneralError) {
                UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                    "",
                    "",
                    "",
                )
            } else if (mIsAuthError) {
                UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                    "",
                    "",
                    "",
                )
            } else if (mIsServerError) {
                UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                    "",
                    "",
                    "",
                )
            } else if (mIsNetworkError) {
                throw NetworkErrorException()
            } else {
                UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    USER_ID,
                    FULL_NAME,
                    IMAGE_URL,
                )
            }
        }
    }

    class UsersCacheTd : UsersCache {
        private val mUsers: MutableList<User?> = ArrayList(1)
        override fun cacheUser(user: User?) {
            val existingUser = getUser(
                user!!.userId,
            )
            if (existingUser != null) {
                mUsers.remove(existingUser)
            }
            mUsers.add(user)
        }

        @Nullable
        override fun getUser(userId: String?): User? {
            for (user in mUsers) {
                if (user!!.userId == userId) {
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
