package com.example.unittestingcourseinkotlin.screens.questiondetails

import com.example.unittestingcourseinkotlin.questions.QuestionDetails
import com.example.unittestingcourseinkotlin.screens.common.views.ObservableViewMvc

interface QuestionDetailsViewMvc : ObservableViewMvc<QuestionDetailsViewMvc.Listener?> {
    interface Listener {
        fun onNavigateUpClicked()
    }

    fun bindQuestion(question: QuestionDetails?)
    fun showProgressIndication()
    fun hideProgressIndication()
}
