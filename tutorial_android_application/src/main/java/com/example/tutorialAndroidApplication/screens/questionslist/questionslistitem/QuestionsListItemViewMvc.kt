package com.example.tutorialAndroidApplication.screens.questionslist.questionslistitem

import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.views.ObservableViewMvc

interface QuestionsListItemViewMvc : ObservableViewMvc<QuestionsListItemViewMvc.Listener> {
    interface Listener {
        fun onQuestionClicked(question: Question?)
    }

    fun bindQuestion(question: Question?)
}
