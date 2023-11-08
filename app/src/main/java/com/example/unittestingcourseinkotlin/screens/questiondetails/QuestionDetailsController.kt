package com.example.unittestingcourseinkotlin.screens.questiondetails

import com.example.unittestingcourseinkotlin.questions.FetchQuestionDetailsUseCase
import com.example.unittestingcourseinkotlin.questions.QuestionDetails
import com.example.unittestingcourseinkotlin.screens.common.screensnavigator.ScreensNavigator
import com.example.unittestingcourseinkotlin.screens.common.toastshelper.ToastsHelper

class QuestionDetailsController(
    private val mFetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
    private val mScreensNavigator: ScreensNavigator,
    private val mToastsHelper: ToastsHelper,
) : QuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener {
    private var mQuestionId: String? = null
    private var mViewMvc: QuestionDetailsViewMvc? = null
    fun bindQuestionId(questionId: String?) {
        mQuestionId = questionId
    }

    fun bindView(viewMvc: QuestionDetailsViewMvc?) {
        mViewMvc = viewMvc
    }

    fun onStart() {
        mViewMvc!!.registerListener(this)
        mFetchQuestionDetailsUseCase.registerListener(this)
        mViewMvc!!.showProgressIndication()
        mFetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(mQuestionId!!)
    }

    fun onStop() {
        mViewMvc!!.unregisterListener(this)
        mFetchQuestionDetailsUseCase.unregisterListener(this)
    }

    override fun onQuestionDetailsFetched(questionDetails: QuestionDetails?) {
        mViewMvc!!.bindQuestion(questionDetails)
        mViewMvc!!.hideProgressIndication()
    }

    override fun onQuestionDetailsFetchFailed() {
        mViewMvc!!.hideProgressIndication()
        mToastsHelper.showUseCaseError()
    }

    override fun onNavigateUpClicked() {
        mScreensNavigator.navigateUp()
    }
}
