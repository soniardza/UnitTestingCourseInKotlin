package com.example.tutorialAndroidApplication.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tutorialAndroidApplication.screens.common.controllers.BaseFragment

class QuestionsListFragment : BaseFragment() {
    private lateinit var questionsListController: QuestionsListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionsListController = getCompositionRoot().getQuestionsListController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewMvc = getCompositionRoot().getViewMvcFactory().getQuestionsListViewMvc(container)
        questionsListController.bindView(viewMvc)
        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        questionsListController.onStart()
    }

    override fun onStop() {
        super.onStop()
        questionsListController.onStop()
    }

    companion object {
        fun newInstance(): Fragment {
            return QuestionsListFragment()
        }
    }
}
