package com.example.unittestingcourseinkotlin.screens.questiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unittestingcourseinkotlin.screens.common.controllers.BaseFragment

class QuestionDetailsFragment : BaseFragment() {
    private var mQuestionDetailsController: QuestionDetailsController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuestionDetailsController = compositionRoot.questionDetailsController
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val mViewMvc = compositionRoot.viewMvcFactory.getQuestionDetailsViewMvc(container)
        mQuestionDetailsController!!.bindView(mViewMvc)
        mQuestionDetailsController!!.bindQuestionId(requireArguments().getString(ARG_QUESTION_ID))
        return mViewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        mQuestionDetailsController!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mQuestionDetailsController!!.onStop()
    }

    private val questionId: String?
        get() = requireArguments().getString(ARG_QUESTION_ID)

    companion object {
        private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"
        fun newInstance(questionId: String?): QuestionDetailsFragment {
            val args = Bundle()
            args.putString(ARG_QUESTION_ID, questionId)
            val fragment = QuestionDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
