package com.example.unittestingcourseinkotlin.screens.questionslist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unittestingcourseinkotlin.questions.Question
import com.example.unittestingcourseinkotlin.screens.common.ViewMvcFactory
import com.example.unittestingcourseinkotlin.screens.questionslist.questionslistitem.QuestionsListItemViewMvc

class QuestionsRecyclerAdapter(
    private val mListener: Listener,
    private val mViewMvcFactory: ViewMvcFactory
) :
    RecyclerView.Adapter<QuestionsRecyclerAdapter.MyViewHolder>(),
    QuestionsListItemViewMvc.Listener {
    interface Listener {
        fun onQuestionClicked(question: Question?)
    }

    class MyViewHolder(val mViewMvc: QuestionsListItemViewMvc) : RecyclerView.ViewHolder(
        mViewMvc.getRootView()!!,
    )

    private var mQuestions: List<Question> = ArrayList()
    fun bindQuestions(questions: List<Question>?) {
        mQuestions = questions?.let { ArrayList(it) }!!
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewMvc: QuestionsListItemViewMvc = mViewMvcFactory.getQuestionsListItemViewMvc(parent)
        viewMvc.registerListener(this)
        return MyViewHolder(viewMvc)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mViewMvc.bindQuestion(mQuestions[position])
    }

    override fun getItemCount(): Int {
        return mQuestions.size
    }

    override fun onQuestionClicked(question: Question?) {
        mListener.onQuestionClicked(question)
    }
}
