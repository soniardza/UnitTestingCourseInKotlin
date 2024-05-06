package com.example.tutorialAndroidApplication.screens.questiondetails

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tutorialAndroidApplication.questions.QuestionDetails
import com.example.tutorialAndroidApplication.screens.common.ViewMvcFactory
import com.example.tutorialAndroidApplication.screens.common.toolbar.ToolbarViewMvc
import com.example.tutorialAndroidApplication.screens.common.views.BaseObservableViewMvc
import com.example.tutorial_android_application.R

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory,
) :
    BaseObservableViewMvc<QuestionDetailsViewMvc.Listener>(),
        QuestionDetailsViewMvc {
    private val toolbarViewMvc: ToolbarViewMvc
    private val toolbar: Toolbar
    private val txtQuestionTitle: TextView
    private val txtQuestionBody: TextView
    private val progressBar: ProgressBar

    init {
        setRootView(inflater.inflate(R.layout.layout_question_details, parent, false))
        txtQuestionTitle = findViewById<TextView>(R.id.txt_question_title)
        txtQuestionBody = findViewById<TextView>(R.id.txt_question_body)
        progressBar = findViewById(R.id.progress)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbarViewMvc = viewMvcFactory.getToolbarViewMvc(toolbar)
        initToolbar()
    }

    private fun initToolbar() {
        toolbar.addView(toolbarViewMvc.getRootView())
        toolbarViewMvc.setTitle(getString(R.string.question_details_screen_title))
        toolbarViewMvc.enableUpButtonAndListen(
            object : ToolbarViewMvc.NavigateUpClickListener {
                override fun onNavigateUpClicked() {
                    for (listener in getListeners()) {
                        listener!!.onNavigateUpClicked()
                    }
                }
            },
        )
    }

    override fun bindQuestion(question: QuestionDetails) {
        val questionTitle = question.title
        val questionBody = question.body

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtQuestionTitle.text = Html.fromHtml(questionTitle, Html.FROM_HTML_MODE_LEGACY)
            txtQuestionBody.text = Html.fromHtml(questionBody, Html.FROM_HTML_MODE_LEGACY)
        } else {
            txtQuestionTitle.text = Html.fromHtml(questionTitle)
            txtQuestionBody.text = Html.fromHtml(questionBody)
        }
    }

    override fun showProgressIndication() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndication() {
        progressBar.visibility = View.GONE
    }
}
