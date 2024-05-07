package com.example.tutorialAndroidApplication.screens.questiondetails

import com.example.tutorialAndroidApplication.questions.FetchQuestionDetailsUseCase
import com.example.tutorialAndroidApplication.screens.common.screensnavidator.ScreensNavigator
import com.example.tutorialAndroidApplication.screens.common.toastshelper.ToastsHelper
import com.example.tutorialAndroidApplication.testdata.QuestionDetailsTestData.getQuestionDetails1
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuestionDetailsControllerSolutionTest {
    // region helper fields ------------------------------------------------------------------------
    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var toastsHelperMock: ToastsHelper

    @Mock
    private lateinit var questionDetailsViewMvcMock: QuestionDetailsViewMvc

    private lateinit var useCaseTd: UseCaseTd

    private lateinit var systemUnderTest: QuestionDetailsController

    // endregion helper fields ---------------------------------------------------------------------

    @Before
    @Throws(Exception::class)
    fun setup() {
        useCaseTd = UseCaseTd()
        systemUnderTest =
            QuestionDetailsController(
                useCaseTd,
                screensNavigatorMock,
                toastsHelperMock,
            )
        systemUnderTest.bindView(questionDetailsViewMvcMock)
        systemUnderTest.bindQuestionId(QUESTION_ID)
    }

    @Test
    @Throws(Exception::class)
    fun onStart_listenersRegistered() {
        // Arrange
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionDetailsViewMvcMock).registerListener(
            systemUnderTest,
        )
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
        verify(questionDetailsViewMvcMock).unregisterListener(
            systemUnderTest,
        )
        useCaseTd.verifyListenerNotRegistered(systemUnderTest)
    }

    @Test
    @Throws(Exception::class)
    fun onStart_success_questionDetailsBoundToView() {
        // Arrange
        success()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionDetailsViewMvcMock).bindQuestion(QUESTION_DETAILS)
    }

    @Test
    @Throws(Exception::class)
    fun onStart_failure_errorToastShown() {
        // Arrange
        failure()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(toastsHelperMock).showUseCaseError()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_progressIndicationShown() {
        // Arrange
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionDetailsViewMvcMock).showProgressIndication()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_success_progressIndicationHidden() {
        // Arrange
        success()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionDetailsViewMvcMock).hideProgressIndication()
    }

    @Test
    @Throws(Exception::class)
    fun onStart_failure_progressIndicationShown() {
        // Arrange
        failure()
        // Act
        systemUnderTest.onStart()
        // Assert
        verify(questionDetailsViewMvcMock).hideProgressIndication()
    }

    @Test
    @Throws(Exception::class)
    fun onNavigateUpClicked_navigatedUp() {
        // Arrange
        // Act
        systemUnderTest.onNavigateUpClicked()
        // Assert
        verify(screensNavigatorMock).navigateUp()
    }

    // region helper methods -----------------------------------------------------------------------
    private fun success() {
        // currently no-op
    }

    private fun failure() {
        useCaseTd.failure = true
    }

    // endregion helper methods --------------------------------------------------------------------
    // region helper classes -----------------------------------------------------------------------
    private class UseCaseTd :
        FetchQuestionDetailsUseCase(null, null) {
        var failure = false

        override fun fetchQuestionDetailsAndNotify(questionId: String) {
            if (questionId != QUESTION_ID) {
                throw RuntimeException("invalid question ID: $questionId")
            }
            for (listener in getListeners) {
                if (failure) {
                    listener?.onQuestionDetailsFetchFailed()
                } else {
                    listener?.onQuestionDetailsFetched(QUESTION_DETAILS)
                }
            }
        }

        fun verifyListenerRegistered(candidate: QuestionDetailsController?) {
            for (listener in getListeners) {
                if (listener === candidate) {
                    return
                }
            }
            throw RuntimeException("listener not registered")
        }

        fun verifyListenerNotRegistered(candidate: QuestionDetailsController?) {
            for (listener in getListeners) {
                if (listener === candidate) {
                    throw RuntimeException("listener registered")
                }
            }
        }
    } // endregion helper classes --------------------------------------------------------------------

    // region constants ----------------------------------------------------------------------------
    companion object {
        private val QUESTION_DETAILS = getQuestionDetails1()
        private val QUESTION_ID = QUESTION_DETAILS.id
    }
    // endregion constants -------------------------------------------------------------------------
}
