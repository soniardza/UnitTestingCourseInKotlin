package com.example.tutorialAndroidApplication.networking.questions

import com.google.gson.annotations.SerializedName

data class QuestionsListResponseSchema(
    @SerializedName("items") val questions: List<QuestionSchema>,
)
