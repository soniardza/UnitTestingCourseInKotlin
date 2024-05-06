package com.example.tutorialAndroidApplication.screens.questionslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.ViewMvcFactory
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerHelper
import com.example.tutorialAndroidApplication.screens.common.toolbar.ToolbarViewMvc
import com.example.tutorialAndroidApplication.screens.common.views.BaseObservableViewMvc
import com.example.tutorial_android_application.R

class QuestionsListViewMvcImpl(
    inflater: LayoutInflater,
    @Nullable parent: ViewGroup?,
    private val navDrawerHelper: NavDrawerHelper,
    private val viewMvcFactory: ViewMvcFactory,
) : BaseObservableViewMvc<QuestionsListViewMvc.Listener>(),
    QuestionsListViewMvc,
    QuestionsRecyclerAdapter.Listener {
    private val toolbarViewMvc: ToolbarViewMvc
    private val toolbar: Toolbar
    private val recyclerQuestions: RecyclerView
    private val adapter: QuestionsRecyclerAdapter
    private val progressBar: ProgressBar

    init {
        setRootView(inflater.inflate(R.layout.layout_questions_list, parent, false))
        recyclerQuestions = findViewById<RecyclerView>(R.id.recycler_questions)
        recyclerQuestions.layoutManager = LinearLayoutManager(context)
        adapter = QuestionsRecyclerAdapter(this, viewMvcFactory)
        recyclerQuestions.adapter = adapter
        progressBar = findViewById(R.id.progress)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbarViewMvc = viewMvcFactory.getToolbarViewMvc(toolbar)
        initToolbar()
    }

    private fun initToolbar() {
        toolbarViewMvc.setTitle(getString(R.string.questions_list_screen_title))
        toolbar.addView(toolbarViewMvc.getRootView())
        toolbarViewMvc.enableHamburgerButtonAndListen(
            object :
                ToolbarViewMvc.HamburgerClickListener {
                override fun onHamburgerClicked() {
                    navDrawerHelper.openDrawer()
                }
            },
        )
    }

    override fun onQuestionClicked(question: Question?) {
        for (listener in getListeners()) {
            listener!!.onQuestionClicked(question!!)
        }
    }

    override fun bindQuestions(questions: List<Question?>?) {
        adapter.bindQuestions(questions)
    }

    override fun showProgressIndication() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndication() {
        progressBar.visibility = View.GONE
    }
}
