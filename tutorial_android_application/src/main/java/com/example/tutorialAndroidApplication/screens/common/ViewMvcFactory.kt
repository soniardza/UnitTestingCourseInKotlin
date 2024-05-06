package com.example.tutorialAndroidApplication.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerHelper
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerViewMvc
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerViewMvcImpl
import com.example.tutorialAndroidApplication.screens.common.toolbar.ToolbarViewMvc
import com.example.tutorialAndroidApplication.screens.questiondetails.QuestionDetailsViewMvc
import com.example.tutorialAndroidApplication.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.example.tutorialAndroidApplication.screens.questionslist.QuestionsListViewMvc
import com.example.tutorialAndroidApplication.screens.questionslist.QuestionsListViewMvcImpl
import com.example.tutorialAndroidApplication.screens.questionslist.questionslistitem.QuestionsListItemViewMvc
import com.example.tutorialAndroidApplication.screens.questionslist.questionslistitem.QuestionsListItemViewMvcImpl

class ViewMvcFactory(
    private val layoutInflater: LayoutInflater,
    private val navDrawerHelper: NavDrawerHelper,
) {
    fun getQuestionsListViewMvc(parent: ViewGroup?): QuestionsListViewMvc {
        return QuestionsListViewMvcImpl(layoutInflater, parent, navDrawerHelper, this)
    }

    fun getQuestionsListItemViewMvc(parent: ViewGroup?): QuestionsListItemViewMvc {
        return QuestionsListItemViewMvcImpl(layoutInflater, parent)
    }

    fun getQuestionDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvc {
        return QuestionDetailsViewMvcImpl(layoutInflater, parent, this)
    }

    fun getToolbarViewMvc(parent: ViewGroup?): ToolbarViewMvc {
        return ToolbarViewMvc(
            layoutInflater,
            parent!!,
        )
    }

    fun getNavDrawerViewMvc(parent: ViewGroup?): NavDrawerViewMvc {
        return NavDrawerViewMvcImpl(layoutInflater, parent)
    }
}
