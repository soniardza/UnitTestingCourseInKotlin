package com.example.tutorialAndroidApplication.questions

import com.example.tutorialAndroidApplication.common.time.TimeProvider
import com.example.tutorialAndroidApplication.networking.questions.FetchQuestionDetailsEndpoint
import com.example.tutorialAndroidApplication.networking.questions.QuestionSchema
import com.example.tutorialAndroidApplication.testdata.QuestionDetailsTestData.getQuestionDetails1
import com.example.tutorialAndroidApplication.testdata.QuestionDetailsTestData.getQuestionDetails2
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class FetchQuestionDetailsUseCaseSolutionTest {
    // region helper fields ------------------------------------------------------------------------
//    @Mock
//    private lateinit var fetchQuestionDetailsEndpointMock: FetchQuestionDetailsEndpoint

    var fetchQuestionDetailsEndpointMock = mock<FetchQuestionDetailsEndpoint>()

    @Mock
    private lateinit var timeProviderMock: TimeProvider

    private lateinit var listener1: ListenerTd

    private lateinit var listener2: ListenerTd

    private var endpointCallsCount = 0

    private lateinit var systemUnderTest: FetchQuestionDetailsUseCase
    // endregion helper fields ---------------------------------------------------------------------

    @Before
    @Throws(Exception::class)
    fun setup() {
        listener1 = ListenerTd()
        listener2 = ListenerTd()
        systemUnderTest = FetchQuestionDetailsUseCase(fetchQuestionDetailsEndpointMock, timeProviderMock)
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_success_listenersNotifiedWithCorrectData() {
        // Arrange
        success()
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        // Assert
        listener1.assertSuccessfulCalls(1)
        assertEquals(QUESTION_DETAILS_1, listener1.lastData)

        listener2.assertSuccessfulCalls(1)
        assertEquals(QUESTION_DETAILS_1, listener2.lastData)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_failure_listenersNotifiedOfFailure() {
        // Arrange
        failure()
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        // Assert
        listener1.assertOneFailingCall()
        listener2.assertOneFailingCall()
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_secondTimeImmediatelyAfterSuccess_listenersNotifiedWithDataFromCache() {
        // Arrange
        success()
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_1, listener1.lastData)

        listener2.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_1, listener2.lastData)

        assertEquals(1, endpointCallsCount)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_secondTimeRightBeforeTimeoutAfterSuccess_listenersNotifiedWithDataFromCache() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT - 1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_1, listener1.lastData)

        listener2.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_1, listener2.lastData)

        assertEquals(1, endpointCallsCount)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_secondTimeRightAfterTimeoutAfterSuccess_listenersNotifiedWithDataFromEndpoint() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_1, listener1.lastData)

        listener2.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_1, listener2.lastData)

        assertEquals(2, endpointCallsCount)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_secondTimeWithDifferentIdAfterSuccess_listenersNotifiedWithDataFromEndpoint() {
        // Arrange
        success()
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_2)
        // Assert
        listener1.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_2, listener1.lastData)

        listener2.assertSuccessfulCalls(2)
        assertEquals(QUESTION_DETAILS_2, listener2.lastData)

        assertEquals(2, endpointCallsCount)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_cacheExpiration_firstQuestionNotifiedWithDataFromCache() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT / 2)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_2)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT - 1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        // Assert
        listener1.assertSuccessfulCalls(3)
        assertEquals(QUESTION_DETAILS_1, listener1.lastData)

        listener2.assertSuccessfulCalls(3)
        assertEquals(QUESTION_DETAILS_1, listener2.lastData)

        assertEquals(2, endpointCallsCount)
    }

    @Test
    @Throws(Exception::class)
    fun fetchQuestionDetailsAndNotify_cacheExpiration_secondQuestionNotifiedWithDataFromCache() {
        // Arrange
        success()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_1)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT / 2)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_2)
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(
            CACHE_TIMEOUT + CACHE_TIMEOUT / 2 - 1,
        )
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID_2)
        // Assert
        listener1.assertSuccessfulCalls(3)
        assertEquals(QUESTION_DETAILS_2, listener1.lastData)

        listener2.assertSuccessfulCalls(3)
        assertEquals(QUESTION_DETAILS_2, listener2.lastData)

        assertEquals(2, endpointCallsCount)
    }

    // region helper methods -----------------------------------------------------------------------
    private fun success() {
        doAnswer { invocation ->
            endpointCallsCount++
            val args = invocation.arguments
            val questionId = args[0] as String
            val listener =
                args[1] as FetchQuestionDetailsEndpoint.Listener
            val response: QuestionSchema =
                when (questionId) {
                    QUESTION_ID_1 -> {
                        QuestionSchema(
                            QUESTION_DETAILS_1.title,
                            QUESTION_DETAILS_1.id,
                            QUESTION_DETAILS_1.body,
                        )
                    }
                    QUESTION_ID_2 -> {
                        QuestionSchema(
                            QUESTION_DETAILS_2.title,
                            QUESTION_DETAILS_2.id,
                            QUESTION_DETAILS_2.body,
                        )
                    }
                    else -> {
                        throw RuntimeException("unhandled question id: $questionId")
                    }
                }
            listener.onQuestionDetailsFetched(response)
            null
        }.`when`(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(
            any(String::class.java),
            any(
                FetchQuestionDetailsEndpoint.Listener::class.java,
            ),
        )
    }

    private fun failure() {
        doAnswer { invocation ->
            endpointCallsCount++
            val args = invocation.arguments
            val listener =
                args[1] as FetchQuestionDetailsEndpoint.Listener
            listener.onQuestionDetailsFetchFailed()
            null
        }.`when`(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(
            any(String::class.java),
            any(
                FetchQuestionDetailsEndpoint.Listener::class.java,
            ),
        )
    }

    // endregion helper methods --------------------------------------------------------------------
    // region helper classes -----------------------------------------------------------------------
    private class ListenerTd : FetchQuestionDetailsUseCase.Listener {
        private var callCount = 0
        private var successCount = 0
        var lastData: QuestionDetails? = null

        override fun onQuestionDetailsFetched(questionDetails: QuestionDetails) {
            callCount++
            successCount++
            lastData = questionDetails
        }

        override fun onQuestionDetailsFetchFailed() {
            callCount++
        }

        fun assertSuccessfulCalls(count: Int) {
            if (callCount != count || callCount != successCount) {
                throw RuntimeException("$count successful call(s) assertion failed; calls: $callCount; successes: $successCount")
            }
        }

        fun assertOneFailingCall() {
            if (callCount != 1 || successCount > 0) {
                throw RuntimeException("one failing call assertion failed")
            }
        }
    }
    // endregion helper classes --------------------------------------------------------------------

    // region constants ----------------------------------------------------------------------------
    companion object {
        private const val CACHE_TIMEOUT: Long = 60000
        private val QUESTION_DETAILS_1 = getQuestionDetails1()
        private val QUESTION_ID_1 = QUESTION_DETAILS_1.id
        private val QUESTION_DETAILS_2 = getQuestionDetails2()
        private val QUESTION_ID_2 = QUESTION_DETAILS_2.id
    }
    // endregion constants -------------------------------------------------------------------------
}
