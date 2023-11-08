package com.example.unittestingcourseinkotlin.screens.questionslist.questionslistitem

import com.example.unittestingcourseinkotlin.questions.Question
import com.example.unittestingcourseinkotlin.screens.common.views.ObservableViewMvc

interface QuestionsListItemViewMvc : ObservableViewMvc<QuestionsListItemViewMvc.Listener?> {
    interface Listener {
        fun onQuestionClicked(question: Question?)
    }

    fun bindQuestion(question: Question?)
}
