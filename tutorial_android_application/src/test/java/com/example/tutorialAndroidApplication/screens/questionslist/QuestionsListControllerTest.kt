package com.example.tutorialAndroidApplication.screens.questionslist

import com.example.tutorialAndroidApplication.common.time.TimeProvider
import com.example.tutorialAndroidApplication.questions.FetchLastActiveQuestionsUseCase
import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.screensnavidator.ScreensNavigator
import com.example.tutorialAndroidApplication.screens.common.toastshelper.ToastsHelper
import com.example.tutorialAndroidApplication.testdata.QuestionsTestData.getQuestion
import com.example.tutorialAndroidApplication.testdata.QuestionsTestData.getQuestions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuestionsListControllerTest {
    // region helper fields ------------------------------------------------------------------------
    @Mock
    private lateinit var screensNavigator: ScreensNavigator

    @Mock
    private lateinit var toastsHelper: ToastsHelper

    @Mock
    private lateinit var questionsListViewMvc: QuestionsListViewMvc

    @Mock
    private lateinit var timeProviderMock: TimeProvider

    private lateinit var useCaseTd: UseCaseTd

    private lateinit var systemUnderTest: QuestionsListController
    // endregion helper fields ---------------------------------------------------------------------

    @Before
    @Throws(Exception::class)
    fun setup() {
        useCaseTd = UseCaseTd()
        systemUnderTest =
            QuestionsListController(
                useCaseTd,
                screensNavigator,
                toastsHelper,
                timeProviderMock,
            )
        systemUnderTest.bindView(questionsListViewMvc)
    }

    @Test
    @Throws(Exception::class)
    fun onStart_progressIndicationShown() {
        // Arrange
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc).showProgressIndication()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_successfulResponse_progressIndicationHidden() {
        // Arrange
        success()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc).hideProgressIndication()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_failure_progressIndicationHidden() {
        // Arrange
        failure()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc).hideProgressIndication()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_successfulResponse_questionsBoundToView() {
        // Arrange
        success()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc).bindQuestions(QUESTIONS)
    }

    @Test
    @Throws(Exception::class)
    fun onStart_secondTimeAfterSuccessfulResponse_questionsBoundToTheViewFromCache() {
        // Arrange
        success()
        // Act
        systemUnderTest.onStart()
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc, times(2)).bindQuestions(
            QUESTIONS,
        )
        verify(useCaseTd, times(1)).callCount
    }

    @Test
    @Throws(Exception::class)
    fun onStart_failure_errorToastShown() {
        // Arrange
        failure()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(toastsHelper).showUseCaseError()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_failure_questionsNotBoundToView() {
        // Arrange
        failure()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc, never()).bindQuestions(anyList<Question>())
    }

    @Test
    @Throws(Exception::class)
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc).registerListener(systemUnderTest)
        useCaseTd.verifyListenerRegistered(systemUnderTest)
    }

    @Test
    @Throws(Exception::class)
    fun onStop_listenersUnregistered() {
        // Arrange
        systemUnderTest.onStart()
        // Act
        systemUnderTest.onStop()
        // Assert
        verify(questionsListViewMvc).unregisterListener(systemUnderTest)
        useCaseTd.verifyListenerNotRegistered(systemUnderTest)
    }

    @Test
    @Throws(Exception::class)
    fun onQuestionClicked_navigatedToQuestionDetailsScreen() {
        // Arrange
        // Act
        systemUnderTest.onQuestionClicked(QUESTION)
        // Assert
        verify(screensNavigator).toQuestionDetails(
            QUESTION.id,
        )
    }

    @Test
    @Throws(Exception::class)
    fun onStart_secondTimeAfterCachingTimeout_questionsBoundToViewFromUseCase() {
        // Arrange
        emptyQuestionsListOnFirstCall()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        systemUnderTest.onStart()
        systemUnderTest.onStop()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(10000L)
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc).bindQuestions(QUESTIONS)
    }

    @Test
    @Throws(Exception::class)
    fun onStart_secondTimeRightBeforeCachingTimeout_questionsBoundToViewFromCache() {
        // Arrange
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(0L)
        // Act
        systemUnderTest.onStart()
        systemUnderTest.onStop()
        `when`(timeProviderMock.getCurrentTimestamp()).thenReturn(9999L)
        systemUnderTest.onStart()
        // Assert
        verify(questionsListViewMvc, times(2)).bindQuestions(
            QUESTIONS,
        )
        verify(useCaseTd, times(1)).callCount
    }

    // region helper methods -----------------------------------------------------------------------
    private fun success() {
        // currently no-op
    }

    private fun failure() {
        useCaseTd.failure = true
    }

    private fun emptyQuestionsListOnFirstCall() {
        useCaseTd.emptyListOnFirstCall = true
    }

    // endregion helper methods --------------------------------------------------------------------
    // region helper classes -----------------------------------------------------------------------
    private class UseCaseTd : FetchLastActiveQuestionsUseCase(null) {
        var emptyListOnFirstCall = false
        var failure = false
        var callCount = 0

        override fun fetchLastActiveQuestionsAndNotify() {
            callCount++
            for (listener in getListeners) {
                if (failure) {
                    listener?.onLastActiveQuestionsFetchFailed()
                } else {
                    if (emptyListOnFirstCall && callCount == 1) {
                        listener?.onLastActiveQuestionsFetched(ArrayList<Question?>())
                    } else {
                        listener?.onLastActiveQuestionsFetched(QUESTIONS)
                    }
                }
            }
        }

        fun verifyListenerRegistered(candidate: QuestionsListController?) {
            for (listener in getListeners) {
                if (listener === candidate) {
                    return
                }
            }
            throw RuntimeException("listener not registered")
        }

        fun verifyListenerNotRegistered(candidate: QuestionsListController?) {
            for (listener in getListeners) {
                if (listener === candidate) {
                    throw RuntimeException("listener not registered")
                }
            }
        }
    }
    // endregion helper classes --------------------------------------------------------------------

    // region constants ----------------------------------------------------------------------------
    companion object {
        private val QUESTIONS: List<Question?> = getQuestions()
        private val QUESTION = getQuestion()
    }
    // endregion constants -------------------------------------------------------------------------
}
