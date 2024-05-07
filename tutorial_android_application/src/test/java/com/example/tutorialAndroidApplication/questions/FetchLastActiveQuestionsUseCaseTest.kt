package com.example.tutorialAndroidApplication.questions

import com.example.tutorialAndroidApplication.networking.questions.FetchLastActiveQuestionsEndpoint
import com.example.tutorialAndroidApplication.networking.questions.QuestionSchema
import com.example.tutorialAndroidApplication.testdata.QuestionsTestData.getQuestions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchLastActiveQuestionsUseCaseTest {
    // region helper fields ------------------------------------------------------------------------
    @Mock
    private lateinit var listener1: FetchLastActiveQuestionsUseCase.Listener

    @Mock
    private lateinit var listener2: FetchLastActiveQuestionsUseCase.Listener

    @Captor
    private lateinit var questionsCaptor: ArgumentCaptor<List<Question?>>

    private lateinit var endpointTd: EndpointTd

    private lateinit var systemUnderTest: FetchLastActiveQuestionsUseCase
    // endregion helper fields ---------------------------------------------------------------------

    @Before
    @Throws(Exception::class)
    fun setup() {
        endpointTd = EndpointTd()
        systemUnderTest = FetchLastActiveQuestionsUseCase(endpointTd)
    }

    @Test
    @Throws(Exception::class)
    fun fetchLastActiveQuestionsAndNotify_success_listenersNotifiedWithCorrectData() {
        // Arrange
        success()
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        // Act
        systemUnderTest.fetchLastActiveQuestionsAndNotify()
        // Assert
        verify(listener1).onLastActiveQuestionsFetched(questionsCaptor.capture())
        verify(listener2).onLastActiveQuestionsFetched(questionsCaptor.capture())

        val questionLists = questionsCaptor.allValues

        assertEquals(QUESTIONS, questionLists[0])
        assertEquals(QUESTIONS, questionLists[1])
    }

    @Test
    @Throws(Exception::class)
    fun fetchLastActiveQuestionsAndNotify_failure_listenersNotifiedOfFailure() {
        // Arrange
        failure()
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        // Act
        systemUnderTest.fetchLastActiveQuestionsAndNotify()
        // Assert
        verify(listener1).onLastActiveQuestionsFetchFailed()
        verify(listener2).onLastActiveQuestionsFetchFailed()
    }

    // region helper methods -----------------------------------------------------------------------
    private fun success() {
        // currently no-op
    }

    private fun failure() {
        endpointTd.failure = true
    }

    // endregion helper methods --------------------------------------------------------------------
    // region helper classes -----------------------------------------------------------------------
    private class EndpointTd : FetchLastActiveQuestionsEndpoint(null) {
        var failure = false

        override fun fetchLastActiveQuestions(listener: Listener) {
            if (failure) {
                listener.onQuestionsFetchFailed()
            } else {
                val questionSchemas: MutableList<QuestionSchema?> = ArrayList()
                questionSchemas.add(QuestionSchema("title1", "id1", "body1"))
                questionSchemas.add(QuestionSchema("title2", "id2", "body2"))
                listener.onQuestionsFetched(questionSchemas)
            }
        }
    }
    // endregion helper classes --------------------------------------------------------------------

    // region constants ----------------------------------------------------------------------------
    companion object {
        private val QUESTIONS: List<Question?> = getQuestions()
    }
    // endregion constants -------------------------------------------------------------------------
}
