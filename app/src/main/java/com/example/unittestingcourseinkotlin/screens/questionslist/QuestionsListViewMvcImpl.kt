package com.example.unittestingcourseinkotlin.screens.questionslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unittestingcourseinkotlin.R
import com.example.unittestingcourseinkotlin.questions.Question
import com.example.unittestingcourseinkotlin.screens.common.ViewMvcFactory
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerHelper
import com.example.unittestingcourseinkotlin.screens.common.toolbar.ToolbarViewMvc
import com.example.unittestingcourseinkotlin.screens.common.toolbar.ToolbarViewMvc.HamburgerClickListener
import com.example.unittestingcourseinkotlin.screens.common.views.BaseObservableViewMvc

class QuestionsListViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val mNavDrawerHelper: NavDrawerHelper,
    viewMvcFactory: ViewMvcFactory,
) : BaseObservableViewMvc<QuestionsListViewMvc.Listener?>(),
    QuestionsListViewMvc,
    QuestionsRecyclerAdapter.Listener {

    private val mToolbarViewMvc: ToolbarViewMvc
    private val mToolbar: Toolbar?
    private val mRecyclerQuestions: RecyclerView?
    private val mAdapter: QuestionsRecyclerAdapter
    private val mProgressBar: ProgressBar?

    init {
        setRootView(inflater.inflate(R.layout.layout_questions_list, parent, false))
        mRecyclerQuestions = findViewById(R.id.recycler_questions)
        mRecyclerQuestions!!.layoutManager = LinearLayoutManager(getContext())
        mAdapter = QuestionsRecyclerAdapter(this, viewMvcFactory)
        mRecyclerQuestions.adapter = mAdapter
        mProgressBar = findViewById(R.id.progress)
        mToolbar = findViewById(R.id.toolbar)
        mToolbarViewMvc = viewMvcFactory.getToolbarViewMvc(mToolbar)
        initToolbar()
    }

    private fun initToolbar() {
        mToolbarViewMvc.setTitle(getString(R.string.questions_list_screen_title))
        mToolbar!!.addView(mToolbarViewMvc.getRootView())
        mToolbarViewMvc.enableHamburgerButtonAndListen(object : HamburgerClickListener {
            override fun onHamburgerClicked() {
                mNavDrawerHelper.openDrawer()
            }
        })
    }

    override fun onQuestionClicked(question: Question?) {
        for (listener in listeners) {
            listener?.onQuestionClicked(question)
        }
    }

    override fun bindQuestions(questions: List<Question?>?) {
        mAdapter.bindQuestions(questions as List<Question>?)
    }

    override fun showProgressIndication() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgressIndication() {
        mProgressBar!!.visibility = View.GONE
    }
}
