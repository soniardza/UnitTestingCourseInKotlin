package com.example.tutorialAndroidApplication.screens.questionslist

import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.views.ObservableViewMvc

interface QuestionsListViewMvc : ObservableViewMvc<QuestionsListViewMvc.Listener> {
    interface Listener {
        fun onQuestionClicked(question: Question)
    }

    fun bindQuestions(questions: List<Question?>?)

    fun showProgressIndication()

    fun hideProgressIndication()
}
