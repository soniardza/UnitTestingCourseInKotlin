package com.example.unittestingcourseinkotlin.questions

import com.example.unittestingcourseinkotlin.common.BaseObservable
import com.example.unittestingcourseinkotlin.networking.questions.FetchLastActiveQuestionsEndpoint
import com.example.unittestingcourseinkotlin.networking.questions.QuestionSchema

class FetchLastActiveQuestionsUseCase(private val mFetchLastActiveQuestionsEndpoint: FetchLastActiveQuestionsEndpoint) :
    BaseObservable<FetchLastActiveQuestionsUseCase.Listener?>() {
    interface Listener {
        fun onLastActiveQuestionsFetched(questions: List<Question>?)
        fun onLastActiveQuestionsFetchFailed()
    }

    fun fetchLastActiveQuestionsAndNotify() {
        mFetchLastActiveQuestionsEndpoint.fetchLastActiveQuestions(object :
            FetchLastActiveQuestionsEndpoint.Listener {
            override fun onQuestionsFetched(questions: List<QuestionSchema?>?) {
                notifySuccess(questions)
            }

            override fun onQuestionsFetchFailed() {
                notifyFailure()
            }
        })
    }

    private fun notifyFailure() {
        for (listener in listeners) {
            listener?.onLastActiveQuestionsFetchFailed()
        }
    }

    private fun notifySuccess(questionSchemas: List<QuestionSchema?>?) {
        val questions: MutableList<Question> = ArrayList(
            questionSchemas!!.size,
        )
        for (questionSchema in questionSchemas) {
            questions.add(Question(questionSchema!!.id, questionSchema.title))
        }
        for (listener in listeners) {
            listener?.onLastActiveQuestionsFetched(questions)
        }
    }
}
