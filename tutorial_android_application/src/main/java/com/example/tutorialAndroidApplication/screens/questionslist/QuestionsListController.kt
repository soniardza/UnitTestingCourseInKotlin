package com.example.tutorialAndroidApplication.screens.questionslist

import com.example.tutorialAndroidApplication.common.time.TimeProvider
import com.example.tutorialAndroidApplication.questions.FetchLastActiveQuestionsUseCase
import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.screensnavidator.ScreensNavigator
import com.example.tutorialAndroidApplication.screens.common.toastshelper.ToastsHelper

class QuestionsListController(
    private val fetchLastActiveQuestionsUseCase: FetchLastActiveQuestionsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastsHelper: ToastsHelper,
    private val timeProvider: TimeProvider,
) : QuestionsListViewMvc.Listener,
    FetchLastActiveQuestionsUseCase.Listener {
    private lateinit var viewMvc: QuestionsListViewMvc
    private var questions: List<Question?>? = null
    private var lastCachedTimestamp: Long = 0

    fun bindView(viewMvc: QuestionsListViewMvc) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        viewMvc!!.registerListener(this)
        fetchLastActiveQuestionsUseCase.registerListener(this)

        if (isCachedDataValid()) {
            viewMvc!!.bindQuestions(questions)
        } else {
            viewMvc!!.showProgressIndication()
            fetchLastActiveQuestionsUseCase.fetchLastActiveQuestionsAndNotify()
        }
    }

    private fun isCachedDataValid(): Boolean {
        return questions != null &&
            timeProvider.getCurrentTimestamp() < lastCachedTimestamp + CACHE_TIMEOUT_MS
    }

    fun onStop() {
        viewMvc!!.unregisterListener(this)
        fetchLastActiveQuestionsUseCase.unregisterListener(this)
    }

    override fun onQuestionClicked(question: Question) {
        screensNavigator.toQuestionDetails(question.id)
    }

    override fun onLastActiveQuestionsFetched(questions: List<Question?>?) {
        this.questions = questions
        lastCachedTimestamp = timeProvider.getCurrentTimestamp()
        viewMvc!!.hideProgressIndication()
        viewMvc!!.bindQuestions(questions)
    }

    override fun onLastActiveQuestionsFetchFailed() {
        viewMvc!!.hideProgressIndication()
        toastsHelper.showUseCaseError()
    }

    companion object {
        private const val CACHE_TIMEOUT_MS = 10000
    }
}
