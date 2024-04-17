package com.example.mockitoFundamentals.example7

import com.example.mockitoFundamentals.example7.authtoken.AuthTokenCache
import com.example.mockitoFundamentals.example7.eventbus.EventBusPoster
import com.example.mockitoFundamentals.example7.eventbus.LoggedInEvent
import com.example.mockitoFundamentals.example7.networking.LoginHttpEndpointSync
import com.example.mockitoFundamentals.example7.networking.NetworkErrorException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.any
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

class LoginUseCaseSyncTest {

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val AUTH_TOKEN = "authToken"
    }

    private lateinit var loginHttpEndpointSyncMock: LoginHttpEndpointSync
    private lateinit var authTokenCacheMock: AuthTokenCache
    private lateinit var eventBusPosterMock: EventBusPoster

    private lateinit var systemUnderTest: LoginUseCaseSync

    object MockitoHelper {
        // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    }

    @Before
    fun setup() {
        loginHttpEndpointSyncMock = mock(LoginHttpEndpointSync::class.java)
        authTokenCacheMock = mock(AuthTokenCache::class.java)
        eventBusPosterMock = mock(EventBusPoster::class.java)
        systemUnderTest = LoginUseCaseSync(loginHttpEndpointSyncMock, authTokenCacheMock, eventBusPosterMock)
        success()
    }

    @Test
    fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
        val captor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(loginHttpEndpointSyncMock, times(1)).loginSync(
            MockitoHelper.capture(captor),
            MockitoHelper.capture(captor)
        )
        val captures = captor.allValues
        assertEquals(USERNAME, captures[0])
        assertEquals(PASSWORD,captures[1])
    }

    @Test
    fun loginSync_success_authTokenCached() {
        val captor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(authTokenCacheMock).cacheAuthToken(MockitoHelper.capture(captor))
        assertEquals(AUTH_TOKEN, captor.value)
    }


    @Test
    fun loginSync_generalError_authTokenNotCached() {
        generalError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(authTokenCacheMock)
    }

    @Test
    fun loginSync_authError_authTokenNotCached() {
        authError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(authTokenCacheMock)
    }

    @Test
    fun loginSync_serverError_authTokenNotCached() {
        serverError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(authTokenCacheMock)
    }

    @Test
    fun loginSync_success_loggedInEventPosted() {
        val captor: ArgumentCaptor<Any> = ArgumentCaptor.forClass(Any::class.java)
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verify(eventBusPosterMock).postEvent(MockitoHelper.capture(captor))
        assertEquals(LoggedInEvent::class.java, captor.value?.javaClass)
    }

    @Test
    fun loginSync_generalError_noInteractionWithEventBusPoster() {
        generalError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun loginSync_authError_noInteractionWithEventBusPoster() {
        authError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun loginSync_serverError_noInteractionWithEventBusPoster() {
        serverError()
        systemUnderTest.loginSync(USERNAME, PASSWORD)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun loginSync_success_successReturned() {
        val result = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.SUCCESS, result)
    }

    @Test
    fun loginSync_serverError_failureReturned() {
        serverError()
        val result = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    @Test
    fun loginSync_authError_failureReturned() {
        authError()
        val result = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    @Test
    fun loginSync_generalError_failureReturned() {
        generalError()
        val result = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    @Test
    fun loginSync_networkError_networkErrorReturned() {
        networkError()
        val result = systemUnderTest.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    private fun networkError() {
        doThrow(NetworkErrorException())
            .`when`(loginHttpEndpointSyncMock)
            .loginSync(any(String::class.java), any(String::class.java))
    }

    private fun success() {
        `when`(loginHttpEndpointSyncMock.loginSync(USERNAME, PASSWORD))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    AUTH_TOKEN
                )
            )
    }

    private fun generalError() {
        `when`(loginHttpEndpointSyncMock.loginSync(USERNAME, PASSWORD))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                    ""
                )
            )
    }

    private fun authError() {
        `when`(loginHttpEndpointSyncMock.loginSync(USERNAME, PASSWORD))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                    ""
                )
            )
    }

    private fun serverError() {
        `when`(loginHttpEndpointSyncMock.loginSync(USERNAME, PASSWORD))
            .thenReturn(
                LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                    ""
                )
            )
    }
}
