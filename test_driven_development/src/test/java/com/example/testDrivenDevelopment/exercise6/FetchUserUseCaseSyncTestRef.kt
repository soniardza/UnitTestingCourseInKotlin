package com.example.testDrivenDevelopment.exercise6

import com.example.testDrivenDevelopment.exercise6.FetchUserUseCaseSync.Status.FAILURE
import com.example.testDrivenDevelopment.exercise6.FetchUserUseCaseSync.Status.NETWORK_ERROR
import com.example.testDrivenDevelopment.exercise6.FetchUserUseCaseSync.Status.SUCCESS
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointResult
import com.example.testDrivenDevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus
import com.example.testDrivenDevelopment.exercise6.networking.NetworkErrorException
import com.example.testDrivenDevelopment.exercise6.users.User
import com.example.testDrivenDevelopment.exercise6.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchUserUseCaseSyncTestRef {
    // region helper fields ------------------------------------------------------------------------
    private lateinit var fetchUserHttpEndpointSyncTestDouble: FetchUserHttpEndpointSyncTestDouble

    @Mock
    private lateinit var usersCacheMock: UsersCache

    object MockitoHelper {
        // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
        fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    }

    // endregion helper fields ---------------------------------------------------------------------

    private lateinit var systemUnderTest: FetchUserUseCaseSync

    @Before
    fun setup() {
        fetchUserHttpEndpointSyncTestDouble = FetchUserHttpEndpointSyncTestDouble()

        // TODO: assign your implementation of FetchUserUseCaseSync to SUT
        systemUnderTest = FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSyncTestDouble, usersCacheMock)

        userNotInCache()
        endpointSuccess()
    }

    @Test
    fun fetchUserSync_notInCache_correctUserIdPassedToEndpoint() {
        // Arrange
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(USER_ID, fetchUserHttpEndpointSyncTestDouble.userId)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointSuccess_successStatus() {
        // Arrange
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(SUCCESS, result?.status)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointSuccess_correctUserReturned() {
        // Arrange
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(USER, result?.user)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointSuccess_userCached() {
        // Arrange
        val captor: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        verify(usersCacheMock).cacheUser(MockitoHelper.capture(captor))
        assertEquals(USER, captor.value)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointAuthError_failureStatus() {
        // Arrange
        endpointAuthError()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(FAILURE, result?.status)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointAuthError_nullUserReturned() {
        // Arrange
        endpointAuthError()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertNull(result?.user)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointAuthError_nothingCached() {
        // Arrange
        endpointAuthError()
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(any(User::class.java))
    }

    @Test
    fun fetchUserSync_notInCacheEndpointServerError_failureStatus() {
        // Arrange
        endpointServerError()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(FAILURE, result?.status)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointServerError_nullUserReturned() {
        // Arrange
        endpointServerError()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertNull(result?.user)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointServerError_nothingCached() {
        // Arrange
        endpointServerError()
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(any(User::class.java))
    }

    @Test
    fun fetchUserSync_notInCacheEndpointNetworkError_failureStatus() {
        // Arrange
        endpointNetworkError()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(NETWORK_ERROR, result?.status)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointNetworkError_nullUserReturned() {
        // Arrange
        endpointNetworkError()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertNull(result?.user)
    }

    @Test
    fun fetchUserSync_notInCacheEndpointNetworkError_nothingCached() {
        // Arrange
        endpointNetworkError()
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        verify(usersCacheMock, never()).cacheUser(any(User::class.java))
    }

    @Test
    fun fetchUserSync_correctUserIdPassedToCache() {
        // Arrange
        val captor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        verify(usersCacheMock).getUser(MockitoHelper.capture(captor))
        assertEquals(USER_ID, captor.value)
    }

    @Test
    fun fetchUserSync_inCache_successStatus() {
        // Arrange
        userInCache()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(SUCCESS, result?.status)
    }

    @Test
    fun fetchUserSync_inCache_cachedUserReturned() {
        // Arrange
        userInCache()
        // Act
        val result = systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(USER, result?.user)
    }

    @Test
    fun fetchUserSync_inCache_endpointNotPolled() {
        // Arrange
        userInCache()
        // Act
        systemUnderTest.fetchUserSync(USER_ID)
        // Assert
        assertEquals(0, fetchUserHttpEndpointSyncTestDouble.requestCount)
    }

    // region helper methods -----------------------------------------------------------------------

    private fun userNotInCache() {
        `when`(usersCacheMock.getUser(anyString())).thenReturn(null)
    }

    private fun userInCache() {
        `when`(usersCacheMock.getUser(anyString())).thenReturn(USER)
    }

    private fun endpointSuccess() {
        // endpoint test double is set up for success by default; this method is for clarity of intent
    }

    private fun endpointAuthError() {
        fetchUserHttpEndpointSyncTestDouble.authError = true
    }

    private fun endpointServerError() {
        fetchUserHttpEndpointSyncTestDouble.serverError = true
    }

    private fun endpointNetworkError() {
        fetchUserHttpEndpointSyncTestDouble.networkError = true
    }

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    private inner class FetchUserHttpEndpointSyncTestDouble : FetchUserHttpEndpointSync {
        var requestCount: Int = 0
        var userId: String = ""
        var authError: Boolean = false
        var serverError: Boolean = false
        var networkError: Boolean = false

        override fun fetchUserSync(userId: String?): EndpointResult? {
            requestCount++
            if (userId != null) {
                this.userId = userId
            }

            return when {
                authError ->
                    EndpointResult(
                        EndpointStatus.AUTH_ERROR,
                        "",
                        "",
                    )
                serverError ->
                    EndpointResult(
                        EndpointStatus.GENERAL_ERROR,
                        "",
                        "",
                    )
                networkError -> throw NetworkErrorException()
                else ->
                    EndpointResult(
                        EndpointStatus.SUCCESS,
                        USER_ID,
                        USERNAME,
                    )
            }
        }
    }

    // endregion helper classes --------------------------------------------------------------------

    // region constants ----------------------------------------------------------------------------
    companion object {
        const val USER_ID = "userId"
        const val USERNAME = "username"
        val USER = User(USER_ID, USERNAME)
    }
    // endregion constants -------------------------------------------------------------------------
}
