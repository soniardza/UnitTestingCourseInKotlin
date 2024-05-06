package com.example.tutorialAndroidApplication.networking.questions

import com.google.gson.annotations.SerializedName

data class QuestionSchema(
    @SerializedName("title") val title: String,
    @SerializedName("question_id") val id: String,
    @SerializedName("body") val body: String,
)
