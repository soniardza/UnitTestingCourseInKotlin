package com.example.unittestingcourseinkotlin.networking

import com.example.unittestingcourseinkotlin.common.Constants
import com.example.unittestingcourseinkotlin.networking.questions.QuestionDetailsResponseSchema
import com.example.unittestingcourseinkotlin.networking.questions.QuestionsListResponseSchema
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackoverflowApi {
    @GET("/questions?key=" + Constants.STACKOVERFLOW_API_KEY + "&sort=activity&order=desc&site=stackoverflow&filter=withbody")
    fun fetchLastActiveQuestions(@Query("pagesize") pageSize: Int?): Call<QuestionsListResponseSchema?>?

    @GET("/questions/{questionId}?key=" + Constants.STACKOVERFLOW_API_KEY + "&site=stackoverflow&filter=withbody")
    fun fetchQuestionDetails(@Path("questionId") questionId: String?): Call<QuestionDetailsResponseSchema?>?
}
