package com.example.tutorialAndroidApplication.questions

import com.example.tutorialAndroidApplication.common.BaseObservable
import com.example.tutorialAndroidApplication.networking.questions.FetchLastActiveQuestionsEndpoint
import com.example.tutorialAndroidApplication.networking.questions.QuestionSchema

open class FetchLastActiveQuestionsUseCase(private val fetchLastActiveQuestionsEndpoint: FetchLastActiveQuestionsEndpoint?) :
    BaseObservable<FetchLastActiveQuestionsUseCase.Listener?>() {
    interface Listener {
        fun onLastActiveQuestionsFetched(questions: List<Question?>?)

        fun onLastActiveQuestionsFetchFailed()
    }

    open fun fetchLastActiveQuestionsAndNotify() {
        fetchLastActiveQuestionsEndpoint?.fetchLastActiveQuestions(
            object :
                FetchLastActiveQuestionsEndpoint.Listener {
                override fun onQuestionsFetched(questions: List<QuestionSchema?>?) {
                    notifySuccess(questions)
                }

                override fun onQuestionsFetchFailed() {
                    notifyFailure()
                }
            },
        )
    }

    private fun notifyFailure() {
        for (listener in getListeners) {
            listener?.onLastActiveQuestionsFetchFailed()
        }
    }

    private fun notifySuccess(questionSchemas: List<QuestionSchema?>?) {
        val questions =
            questionSchemas?.map { questionSchema ->
                questionSchema?.let { schema ->
                    Question(schema.id, schema.title)
                }
            }
        for (listener in getListeners) {
            listener?.onLastActiveQuestionsFetched(questions)
        }
    }
}
