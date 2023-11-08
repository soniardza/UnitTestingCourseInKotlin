package com.example.unittestingcourseinkotlin.screens.questiondetails

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.unittestingcourseinkotlin.R
import com.example.unittestingcourseinkotlin.questions.QuestionDetails
import com.example.unittestingcourseinkotlin.screens.common.ViewMvcFactory
import com.example.unittestingcourseinkotlin.screens.common.toolbar.ToolbarViewMvc
import com.example.unittestingcourseinkotlin.screens.common.toolbar.ToolbarViewMvc.NavigateUpClickListener
import com.example.unittestingcourseinkotlin.screens.common.views.BaseObservableViewMvc

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory,
) :
    BaseObservableViewMvc<QuestionDetailsViewMvc.Listener?>(),
    QuestionDetailsViewMvc {
    private val mToolbarViewMvc: ToolbarViewMvc
    private val mToolbar: Toolbar?
    private val mTxtQuestionTitle: TextView?
    private val mTxtQuestionBody: TextView?
    private val mProgressBar: ProgressBar?

    init {
        setRootView(inflater.inflate(R.layout.layout_question_details, parent, false))
        mTxtQuestionTitle = findViewById(R.id.txt_question_title)
        mTxtQuestionBody = findViewById(R.id.txt_question_body)
        mProgressBar = findViewById(R.id.progress)
        mToolbar = findViewById(R.id.toolbar)
        mToolbarViewMvc = viewMvcFactory.getToolbarViewMvc(mToolbar)
        initToolbar()
    }

    private fun initToolbar() {
        mToolbar!!.addView(mToolbarViewMvc.getRootView())
        mToolbarViewMvc.setTitle(getString(R.string.question_details_screen_title))
        mToolbarViewMvc.enableUpButtonAndListen(object : NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener?.onNavigateUpClicked()
                }
            }
        })
    }

    override fun bindQuestion(question: QuestionDetails?) {
        val questionTitle = question!!.title
        val questionBody = question.body
        mTxtQuestionTitle!!.text = Html.fromHtml(questionTitle, Html.FROM_HTML_MODE_LEGACY)
        mTxtQuestionBody!!.text = Html.fromHtml(questionBody, Html.FROM_HTML_MODE_LEGACY)
    }

    override fun showProgressIndication() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgressIndication() {
        mProgressBar!!.visibility = View.GONE
    }
}
