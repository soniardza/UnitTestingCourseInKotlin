package com.example.unittestingcourseinkotlin.networking.questions

import com.example.unittestingcourseinkotlin.networking.StackoverflowApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchQuestionDetailsEndpoint(private val mStackoverflowApi: StackoverflowApi) {
    interface Listener {
        fun onQuestionDetailsFetched(question: QuestionSchema?)
        fun onQuestionDetailsFetchFailed()
    }

    fun fetchQuestionDetails(questionId: String?, listener: Listener) {
        mStackoverflowApi.fetchQuestionDetails(questionId)
            ?.enqueue(
                object : Callback<QuestionDetailsResponseSchema?> {
                    override fun onResponse(
                        call: Call<QuestionDetailsResponseSchema?>,
                        response: Response<QuestionDetailsResponseSchema?>,
                    ) {
                        if (response.isSuccessful) {
                            listener.onQuestionDetailsFetched(response.body()!!.getQuestion())
                        } else {
                            listener.onQuestionDetailsFetchFailed()
                        }
                    }

                    override fun onFailure(call: Call<QuestionDetailsResponseSchema?>, t: Throwable) {
                        listener.onQuestionDetailsFetchFailed()
                    }
                },
            )
    }
}
