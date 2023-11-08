package com.example.unittestingcourseinkotlin.networking.questions

import com.example.unittestingcourseinkotlin.common.Constants
import com.example.unittestingcourseinkotlin.networking.StackoverflowApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchLastActiveQuestionsEndpoint(private val mStackoverflowApi: StackoverflowApi) {

    interface Listener {
        fun onQuestionsFetched(questions: List<QuestionSchema>)
        fun onQuestionsFetchFailed()
    }

    fun fetchLastActiveQuestions(listener: Listener) {
        mStackoverflowApi.fetchLastActiveQuestions(Constants.QUESTIONS_LIST_PAGE_SIZE)
            ?.enqueue(object : Callback<QuestionsListResponseSchema?> {
                override fun onResponse(
                    call: Call<QuestionsListResponseSchema?>,
                    response: Response<QuestionsListResponseSchema?>,
                ) {
                    if (response.isSuccessful) {
                        listener.onQuestionsFetched(response.body()?.questions ?: emptyList())
                    } else {
                        listener.onQuestionsFetchFailed()
                    }
                }

                override fun onFailure(call: Call<QuestionsListResponseSchema?>, t: Throwable) {
                    listener.onQuestionsFetchFailed()
                }
            })
    }
}
