package com.example.tutorialAndroidApplication.screens.common.screensnavidator

import com.example.tutorialAndroidApplication.screens.common.fragmentframehelper.FragmentFrameHelper
import com.example.tutorialAndroidApplication.screens.questiondetails.QuestionDetailsFragment
import com.example.tutorialAndroidApplication.screens.questionslist.QuestionsListFragment

class ScreensNavigator(private val fragmentFrameHelper: FragmentFrameHelper) {
    fun toQuestionDetails(questionId: String) {
        fragmentFrameHelper.replaceFragment(QuestionDetailsFragment.newInstance(questionId))
    }

    fun toQuestionsList() {
        fragmentFrameHelper.replaceFragmentAndClearBackstack(QuestionsListFragment.newInstance())
    }

    fun navigateUp() {
        fragmentFrameHelper.navigateUp()
    }
}
