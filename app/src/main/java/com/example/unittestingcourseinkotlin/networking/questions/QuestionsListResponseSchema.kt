package com.example.unittestingcourseinkotlin.networking.questions

import com.google.gson.annotations.SerializedName

class QuestionsListResponseSchema(
    @SerializedName("items") val questions: List<QuestionSchema>
)
