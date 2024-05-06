package com.example.tutorialAndroidApplication.screens.questiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.tutorialAndroidApplication.screens.common.controllers.BaseFragment

class QuestionDetailsFragment : BaseFragment() {
    companion object {
        private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"

        fun newInstance(questionId: String): QuestionDetailsFragment {
            val args = Bundle()
            args.putString(ARG_QUESTION_ID, questionId)
            val fragment = QuestionDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var questionDetailsController: QuestionDetailsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionDetailsController = getCompositionRoot().getQuestionDetailsController()
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewMvc = getCompositionRoot().getViewMvcFactory().getQuestionDetailsViewMvc(container)

        questionDetailsController.bindView(viewMvc)
        questionDetailsController.bindQuestionId(arguments?.getString(ARG_QUESTION_ID))

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        questionDetailsController.onStart()
    }

    override fun onStop() {
        super.onStop()
        questionDetailsController.onStop()
    }

    private fun getQuestionId(): String? {
        return arguments?.getString(ARG_QUESTION_ID)
    }
}
