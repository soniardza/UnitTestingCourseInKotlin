package com.example.unittestingcourseinkotlin.networking.questions

import com.google.gson.annotations.SerializedName

class QuestionDetailsResponseSchema(question: QuestionSchema) {
    @SerializedName("items")
    private val mQuestions: List<QuestionSchema> = listOf(question)

    fun getQuestion(): QuestionSchema {
        return mQuestions[0]
    }
}
