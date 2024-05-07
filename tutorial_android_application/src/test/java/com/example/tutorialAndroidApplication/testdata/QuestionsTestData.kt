package com.example.tutorialAndroidApplication.testdata

import com.example.tutorialAndroidApplication.questions.Question

object QuestionsTestData {
    fun getQuestion(): Question {
        return Question("id", "title")
    }

    fun getQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        questions.add(Question("id1", "title1"))
        questions.add(Question("id2", "title2"))
        return questions
    }
}
