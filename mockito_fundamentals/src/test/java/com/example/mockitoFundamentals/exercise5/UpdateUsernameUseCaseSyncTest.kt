package com.example.mockitoFundamentals.exercise5

import com.example.mockitoFundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult
import com.example.mockitoFundamentals.exercise5.eventbus.EventBusPoster
import com.example.mockitoFundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.example.mockitoFundamentals.exercise5.networking.NetworkErrorException
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult
import com.example.mockitoFundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus
import com.example.mockitoFundamentals.exercise5.users.User
import com.example.mockitoFundamentals.exercise5.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

class ExerciseSolution5 {
    companion object {
        const val USER_ID = "userId"
        const val USERNAME = "username"
    }

    private lateinit var updateUsernameHttpEndpointSyncMock: UpdateUsernameHttpEndpointSync
    private lateinit var usersCacheMock: UsersCache
    private lateinit var eventBusPosterMock: EventBusPoster
    private lateinit var systemUnderTest: UpdateUsernameUseCaseSync

    object MockitoHelper {
        // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    }

    @Before
    fun setup() {
        updateUsernameHttpEndpointSyncMock = mock(UpdateUsernameHttpEndpointSync::class.java)
        usersCacheMock = mock(UsersCache::class.java)
        eventBusPosterMock = mock(EventBusPoster::class.java)
        systemUnderTest = UpdateUsernameUseCaseSync(
            updateUsernameHttpEndpointSyncMock, usersCacheMock, eventBusPosterMock
        )
        success()
    }

    @Test
    fun updateUsername_success_userIdAndUsernamePassedToEndpoint() {
        val captor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verify(updateUsernameHttpEndpointSyncMock, times(1)).updateUsername(
            MockitoHelper.capture(captor), MockitoHelper.capture(captor)
        )

        val captures = captor.allValues
        assertEquals(USER_ID, captures[0])
        assertEquals(USERNAME, captures[1])
    }

    @Test
    fun updateUsername_success_userCached() {
        val captor: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)

        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verify(usersCacheMock).cacheUser(MockitoHelper.capture(captor))

        val cachedUser = captor.value
        assertEquals(USER_ID, cachedUser.userId)
        assertEquals(USERNAME, cachedUser.username)
    }

    @Test
    fun updateUsername_generalError_userNotCached() {
        generalError()
        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun updateUsername_authError_userNotCached() {
        authError()
        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun updateUsername_serverError_userNotCached() {
        serverError()
        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun updateUsername_success_loggedInEventPosted() {
        val captor: ArgumentCaptor<Any> = ArgumentCaptor.forClass(Any::class.java)

        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verify(eventBusPosterMock).postEvent(captor.capture())
        assertEquals(UserDetailsChangedEvent::class.java, captor.value?.javaClass)
    }

    @Test
    fun updateUsername_generalError_noInteractionWithEventBusPoster() {
        generalError()
        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun updateUsername_authError_noInteractionWithEventBusPoster() {
        authError()
        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun updateUsername_serverError_noInteractionWithEventBusPoster() {
        serverError()
        systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun updateUsername_success_successReturned() {
        val result = systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        assertEquals(UseCaseResult.SUCCESS, result)
    }

    @Test
    fun updateUsername_serverError_failureReturned() {
        serverError()
        val result = systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun updateUsername_authError_failureReturned() {
        authError()
        val result = systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun updateUsername_generalError_failureReturned() {
        generalError()
        val result = systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun updateUsername_networkError_networkErrorReturned() {
        networkError()
        val result = systemUnderTest.updateUsernameSync(USER_ID, USERNAME)
        assertEquals(UseCaseResult.NETWORK_ERROR, result)
    }

    private fun networkError() {
        doThrow(NetworkErrorException()).`when`(updateUsernameHttpEndpointSyncMock)
            .updateUsername(anyString(), anyString())
    }

    private fun success() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                anyString(),
                anyString()
            )
        ).thenReturn(
                EndpointResult(
                    EndpointResultStatus.SUCCESS, USER_ID, USERNAME
                )
            )
    }

    private fun generalError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                anyString(),
                anyString()
            )
        ).thenReturn(EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", ""))
    }

    private fun authError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                anyString(),
                anyString()
            )
        ).thenReturn(EndpointResult(EndpointResultStatus.AUTH_ERROR, "", ""))
    }

    private fun serverError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                anyString(),
                anyString()
            )
        ).thenReturn(EndpointResult(EndpointResultStatus.SERVER_ERROR, "", ""))
    }
}
