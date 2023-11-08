package com.example.unittestingcourseinkotlin.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unittestingcourseinkotlin.screens.common.controllers.BaseFragment

class QuestionsListFragment : BaseFragment() {
    private var mQuestionsListController: QuestionsListController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuestionsListController = compositionRoot.questionsListController
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewMvc = compositionRoot.viewMvcFactory.getQuestionsListViewMvc(container)
        mQuestionsListController!!.bindView(viewMvc)
        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        mQuestionsListController!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mQuestionsListController!!.onStop()
    }

    companion object {
        fun newInstance(): Fragment {
            return QuestionsListFragment()
        }
    }
}
