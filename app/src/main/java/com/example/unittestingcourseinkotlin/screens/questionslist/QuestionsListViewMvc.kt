package com.example.unittestingcourseinkotlin.screens.questionslist

import com.example.unittestingcourseinkotlin.questions.Question
import com.example.unittestingcourseinkotlin.screens.common.views.ObservableViewMvc

interface QuestionsListViewMvc : ObservableViewMvc<QuestionsListViewMvc.Listener?> {
    interface Listener {
        fun onQuestionClicked(question: Question?)
    }

    fun bindQuestions(questions: List<Question?>?)
    fun showProgressIndication()
    fun hideProgressIndication()
}
