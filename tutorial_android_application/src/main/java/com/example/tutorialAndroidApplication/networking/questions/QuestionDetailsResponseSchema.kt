package com.example.tutorialAndroidApplication.networking.questions

import com.google.gson.annotations.SerializedName

data class QuestionDetailsResponseSchema(
    @SerializedName("items") val questions: List<QuestionSchema>,
) {
    val question: QuestionSchema
        get() = questions[0]
}
