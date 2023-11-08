package com.example.unittestingcourseinkotlin.screens.common.screensnavigator

import com.example.unittestingcourseinkotlin.screens.common.fragmentframehelper.FragmentFrameHelper
import com.example.unittestingcourseinkotlin.screens.questiondetails.QuestionDetailsFragment
import com.example.unittestingcourseinkotlin.screens.questionslist.QuestionsListFragment

class ScreensNavigator(private val mFragmentFrameHelper: FragmentFrameHelper) {
    fun toQuestionDetails(questionId: String?) {
        mFragmentFrameHelper.replaceFragment(QuestionDetailsFragment.newInstance(questionId))
    }

    fun toQuestionsList() {
        mFragmentFrameHelper.replaceFragmentAndClearBackstack(QuestionsListFragment.newInstance())
    }

    fun navigateUp() {
        mFragmentFrameHelper.navigateUp()
    }
}
