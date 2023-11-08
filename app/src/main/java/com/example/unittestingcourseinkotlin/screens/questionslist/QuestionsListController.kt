package com.example.unittestingcourseinkotlin.screens.questionslist

import com.example.unittestingcourseinkotlin.common.time.TimeProvider
import com.example.unittestingcourseinkotlin.questions.FetchLastActiveQuestionsUseCase
import com.example.unittestingcourseinkotlin.questions.Question
import com.example.unittestingcourseinkotlin.screens.common.screensnavigator.ScreensNavigator
import com.example.unittestingcourseinkotlin.screens.common.toastshelper.ToastsHelper

class QuestionsListController(
    private val mFetchLastActiveQuestionsUseCase: FetchLastActiveQuestionsUseCase,
    private val mScreensNavigator: ScreensNavigator,
    private val mToastsHelper: ToastsHelper,
    private val mTimeProvider: TimeProvider,
) : QuestionsListViewMvc.Listener,
    FetchLastActiveQuestionsUseCase.Listener {
    private var mViewMvc: QuestionsListViewMvc? = null
    private var mQuestions: List<Question>? = null
    private var mLastCachedTimestamp: Long = 0
    fun bindView(viewMvc: QuestionsListViewMvc?) {
        mViewMvc = viewMvc
    }

    fun onStart() {
        mViewMvc!!.registerListener(this)
        mFetchLastActiveQuestionsUseCase.registerListener(this)
        if (isCachedDataValid) {
            mViewMvc!!.bindQuestions(mQuestions)
        } else {
            mViewMvc!!.showProgressIndication()
            mFetchLastActiveQuestionsUseCase.fetchLastActiveQuestionsAndNotify()
        }
    }

    private val isCachedDataValid: Boolean
        private get() = (
            mQuestions != null &&
                mTimeProvider.currentTimestamp < mLastCachedTimestamp + CACHE_TIMEOUT_MS
            )

    fun onStop() {
        mViewMvc!!.unregisterListener(this)
        mFetchLastActiveQuestionsUseCase.unregisterListener(this)
    }

    override fun onQuestionClicked(question: Question?) {
        mScreensNavigator.toQuestionDetails(question!!.id)
    }

    override fun onLastActiveQuestionsFetched(questions: List<Question>?) {
        mQuestions = questions
        mLastCachedTimestamp = mTimeProvider.currentTimestamp
        mViewMvc!!.hideProgressIndication()
        mViewMvc!!.bindQuestions(questions)
    }

    override fun onLastActiveQuestionsFetchFailed() {
        mViewMvc!!.hideProgressIndication()
        mToastsHelper.showUseCaseError()
    }

    companion object {
        private const val CACHE_TIMEOUT_MS = 10000
    }
}
