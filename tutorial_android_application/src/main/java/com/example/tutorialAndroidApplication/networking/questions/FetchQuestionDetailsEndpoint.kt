package com.example.tutorialAndroidApplication.networking.questions

import com.example.tutorialAndroidApplication.networking.StackoverflowApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestionDetailsEndpoint(private val stackoverflowApi: StackoverflowApi) {
    interface Listener {
        fun onQuestionDetailsFetched(question: QuestionSchema?)

        fun onQuestionDetailsFetchFailed()
    }

    fun fetchQuestionDetails(
        questionId: String?,
        listener: Listener,
    ) {
        stackoverflowApi.fetchQuestionDetails(questionId!!)
            .enqueue(
                object : Callback<QuestionDetailsResponseSchema> {
                    override fun onResponse(
                        call: Call<QuestionDetailsResponseSchema>,
                        response: Response<QuestionDetailsResponseSchema>,
                    ) {
                        if (response.isSuccessful) {
                            listener.onQuestionDetailsFetched(response.body()?.question)
                        } else {
                            listener.onQuestionDetailsFetchFailed()
                        }
                    }

                    override fun onFailure(
                        call: Call<QuestionDetailsResponseSchema>,
                        t: Throwable,
                    ) {
                        listener.onQuestionDetailsFetchFailed()
                    }
                },
            )
    }
}
