package com.example.tutorialAndroidApplication.testdata

import com.example.tutorialAndroidApplication.questions.QuestionDetails

object QuestionDetailsTestData {
    fun getQuestionDetails1(): QuestionDetails {
        return QuestionDetails("id1", "title1", "body1")
    }

    fun getQuestionDetails2(): QuestionDetails {
        return QuestionDetails("id2", "title2", "body2")
    }
}
