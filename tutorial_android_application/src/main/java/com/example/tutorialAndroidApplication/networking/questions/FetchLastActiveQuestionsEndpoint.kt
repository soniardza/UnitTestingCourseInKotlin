package com.example.tutorialAndroidApplication.networking.questions

import com.example.tutorialAndroidApplication.common.Constants
import com.example.tutorialAndroidApplication.networking.StackoverflowApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FetchLastActiveQuestionsEndpoint(private val stackoverflowApi: StackoverflowApi?) {
    interface Listener {
        fun onQuestionsFetched(questions: List<QuestionSchema?>?)

        fun onQuestionsFetchFailed()
    }

    open fun fetchLastActiveQuestions(listener: Listener) {
        stackoverflowApi?.fetchLastActiveQuestions(Constants.QUESTIONS_LIST_PAGE_SIZE)
            ?.enqueue(
                object : Callback<QuestionsListResponseSchema?> {
                    override fun onResponse(
                        call: Call<QuestionsListResponseSchema?>?,
                        response: Response<QuestionsListResponseSchema?>,
                    ) {
                        if (response.isSuccessful) {
                            listener.onQuestionsFetched(response.body()?.questions)
                        } else {
                            listener.onQuestionsFetchFailed()
                        }
                    }

                    override fun onFailure(
                        call: Call<QuestionsListResponseSchema?>?,
                        t: Throwable?,
                    ) {
                        listener.onQuestionsFetchFailed()
                    }
                },
            )
    }
}
