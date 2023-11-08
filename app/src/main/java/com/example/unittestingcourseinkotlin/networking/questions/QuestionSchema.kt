package com.example.unittestingcourseinkotlin.networking.questions

import com.google.gson.annotations.SerializedName

class QuestionSchema(
    @SerializedName("title") val title: String,
    @SerializedName(
        "question_id",
    ) val id: String,
    @SerializedName("body") val body: String,
)
