package com.example.tutorialAndroidApplication.screens.questiondetails

import com.example.tutorialAndroidApplication.questions.QuestionDetails
import com.example.tutorialAndroidApplication.screens.common.views.ObservableViewMvc

interface QuestionDetailsViewMvc : ObservableViewMvc<QuestionDetailsViewMvc.Listener> {
    interface Listener {
        fun onNavigateUpClicked()
    }

    fun bindQuestion(question: QuestionDetails)

    fun showProgressIndication()

    fun hideProgressIndication()
}
