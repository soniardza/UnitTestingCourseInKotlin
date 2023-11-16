package com.example.testDoublesFundamentals.example4

import com.example.testDoublesFundamentals.example4.LoginUseCaseSync.UseCaseResult
import com.example.testDoublesFundamentals.example4.authtoken.AuthTokenCache
import com.example.testDoublesFundamentals.example4.eventbus.EventBusPoster
import com.example.testDoublesFundamentals.example4.eventbus.LoggedInEvent
import com.example.testDoublesFundamentals.example4.networking.LoginHttpEndpointSync
import com.example.testDoublesFundamentals.example4.networking.NetworkErrorException
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class LoginUseCaseSyncTest {
    var mLoginHttpEndpointSyncTd: LoginHttpEndpointSyncTd? = null
    var mAuthTokenCacheTd: AuthTokenCacheTd? = null
    var mEventBusPosterTd: EventBusPosterTd? = null
    private var SUT: LoginUseCaseSync? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        mLoginHttpEndpointSyncTd = LoginHttpEndpointSyncTd()
        mAuthTokenCacheTd = AuthTokenCacheTd()
        mEventBusPosterTd = EventBusPosterTd()
        SUT = LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(mLoginHttpEndpointSyncTd!!.mUsername, `is`(USERNAME))
        assertThat(mLoginHttpEndpointSyncTd!!.mPassword, `is`(PASSWORD))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_authTokenCached() {
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(mAuthTokenCacheTd!!.getAuthToken(), `is`(AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd!!.mIsGeneralError = true
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(
            mAuthTokenCacheTd!!.getAuthToken(),
            `is`(
                NON_INITIALIZED_AUTH_TOKEN,
            ),
        )
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd!!.mIsAuthError = true
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(
            mAuthTokenCacheTd!!.getAuthToken(),
            `is`(
                NON_INITIALIZED_AUTH_TOKEN,
            ),
        )
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd!!.mIsServerError = true
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(
            mAuthTokenCacheTd!!.getAuthToken(),
            `is`(
                NON_INITIALIZED_AUTH_TOKEN,
            ),
        )
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_loggedInEventPosted() {
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(
            mEventBusPosterTd!!.mEvent,
            `is`(
                instanceOf(
                    LoggedInEvent::class.java,
                ),
            ),
        )
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd!!.mIsGeneralError = true
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd!!.mInteractionsCount, `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd!!.mIsAuthError = true
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd!!.mInteractionsCount, `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd!!.mIsServerError = true
        SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(mEventBusPosterTd!!.mInteractionsCount, `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_successReturned() {
        val result: UseCaseResult? = SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(result, `is`(UseCaseResult.SUCCESS))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_failureReturned() {
        mLoginHttpEndpointSyncTd!!.mIsServerError = true
        val result: UseCaseResult? = SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_failureReturned() {
        mLoginHttpEndpointSyncTd!!.mIsAuthError = true
        val result: UseCaseResult? = SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_failureReturned() {
        mLoginHttpEndpointSyncTd!!.mIsGeneralError = true
        val result: UseCaseResult? = SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_networkError_networkErrorReturned() {
        mLoginHttpEndpointSyncTd!!.mIsNetworkError = true
        val result: UseCaseResult? = SUT!!.loginSync(USERNAME, PASSWORD)
        assertThat(result, `is`(UseCaseResult.NETWORK_ERROR))
    }

    // ---------------------------------------------------------------------------------------------
    // Helper classes
    class LoginHttpEndpointSyncTd : LoginHttpEndpointSync {
        var mUsername: String? = ""
        var mPassword: String? = ""
        var mIsGeneralError = false
        var mIsAuthError = false
        var mIsServerError = false
        var mIsNetworkError = false

        @Throws(NetworkErrorException::class)
        override fun loginSync(
            username: String?,
            password: String?,
        ): LoginHttpEndpointSync.EndpointResult {
            mUsername = username
            mPassword = password
            return if (mIsGeneralError) {
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                    "",
                )
            } else if (mIsAuthError) {
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                    "",
                )
            } else if (mIsServerError) {
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                    "",
                )
            } else if (mIsNetworkError) {
                throw NetworkErrorException()
            } else {
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    AUTH_TOKEN,
                )
            }
        }
    }

    class AuthTokenCacheTd : AuthTokenCache {
        var mAuthToken: String? = NON_INITIALIZED_AUTH_TOKEN
        override fun cacheAuthToken(authToken: String?) {
            mAuthToken = authToken
        }

        override fun getAuthToken(): String? {
            return mAuthToken
        }
    }

    class EventBusPosterTd : EventBusPoster {
        var mEvent: Any? = null
        var mInteractionsCount = 0
        override fun postEvent(event: Any?) {
            mInteractionsCount++
            mEvent = event
        }
    }

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val AUTH_TOKEN = "authToken"
        const val NON_INITIALIZED_AUTH_TOKEN = "noAuthToken"
    }
}
