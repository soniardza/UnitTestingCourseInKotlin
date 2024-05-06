package com.example.tutorialAndroidApplication.screens.questiondetails

import com.example.tutorialAndroidApplication.questions.FetchQuestionDetailsUseCase
import com.example.tutorialAndroidApplication.questions.QuestionDetails
import com.example.tutorialAndroidApplication.screens.common.screensnavidator.ScreensNavigator
import com.example.tutorialAndroidApplication.screens.common.toastshelper.ToastsHelper

class QuestionDetailsController(
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastsHelper: ToastsHelper,
) : QuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener {
    private var questionId: String? = null
    private var viewMvc: QuestionDetailsViewMvc? = null

    fun bindQuestionId(questionId: String?) {
        this.questionId = questionId
    }

    fun bindView(viewMvc: QuestionDetailsViewMvc?) {
        this.viewMvc = viewMvc
    }

    fun onStart() {
        this.viewMvc!!.registerListener(this)
        fetchQuestionDetailsUseCase.registerListener(this)
        this.viewMvc!!.showProgressIndication()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(questionId!!)
    }

    fun onStop() {
        viewMvc!!.unregisterListener(this)
        fetchQuestionDetailsUseCase.unregisterListener(this)
    }

    override fun onQuestionDetailsFetched(questionDetails: QuestionDetails) {
        viewMvc!!.bindQuestion(questionDetails)
        viewMvc!!.hideProgressIndication()
    }

    override fun onQuestionDetailsFetchFailed() {
        viewMvc!!.hideProgressIndication()
        toastsHelper.showUseCaseError()
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }
}
