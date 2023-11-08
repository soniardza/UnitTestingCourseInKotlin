package com.example.unittestingcourseinkotlin.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerHelper
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerViewMvc
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerViewMvcImpl
import com.example.unittestingcourseinkotlin.screens.common.toolbar.ToolbarViewMvc
import com.example.unittestingcourseinkotlin.screens.questiondetails.QuestionDetailsViewMvc
import com.example.unittestingcourseinkotlin.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.example.unittestingcourseinkotlin.screens.questionslist.QuestionsListViewMvc
import com.example.unittestingcourseinkotlin.screens.questionslist.QuestionsListViewMvcImpl
import com.example.unittestingcourseinkotlin.screens.questionslist.questionslistitem.QuestionsListItemViewMvc
import com.example.unittestingcourseinkotlin.screens.questionslist.questionslistitem.QuestionsListItemViewMvcImpl

class ViewMvcFactory(
    private val mLayoutInflater: LayoutInflater,
    private val mNavDrawerHelper: NavDrawerHelper
) {
    fun getQuestionsListViewMvc(parent: ViewGroup?): QuestionsListViewMvc {
        return QuestionsListViewMvcImpl(mLayoutInflater, parent, mNavDrawerHelper, this)
    }

    fun getQuestionsListItemViewMvc(parent: ViewGroup?): QuestionsListItemViewMvc {
        return QuestionsListItemViewMvcImpl(mLayoutInflater, parent)
    }

    fun getQuestionDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvc {
        return QuestionDetailsViewMvcImpl(mLayoutInflater, parent, this)
    }

    fun getToolbarViewMvc(parent: ViewGroup?): ToolbarViewMvc {
        return ToolbarViewMvc(mLayoutInflater, parent)
    }

    fun getNavDrawerViewMvc(parent: ViewGroup?): NavDrawerViewMvc {
        return NavDrawerViewMvcImpl(mLayoutInflater, parent)
    }
}
