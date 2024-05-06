package com.example.tutorialAndroidApplication.screens.questionslist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.ViewMvcFactory
import com.example.tutorialAndroidApplication.screens.questionslist.questionslistitem.QuestionsListItemViewMvc

class QuestionsRecyclerAdapter(
    private val listener: Listener,
    private val viewMvcFactory: ViewMvcFactory,
) :
    RecyclerView.Adapter<QuestionsRecyclerAdapter.MyViewHolder>(), QuestionsListItemViewMvc.Listener {
    interface Listener {
        fun onQuestionClicked(question: Question?)
    }

    class MyViewHolder(val viewMvc: QuestionsListItemViewMvc) :
        RecyclerView.ViewHolder(viewMvc.getRootView())

    private var questions: List<Question?> = ArrayList()

    fun bindQuestions(questions: List<Question?>?) {
        this.questions = ArrayList(questions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val viewMvc = viewMvcFactory.getQuestionsListItemViewMvc(parent)
        viewMvc.registerListener(this)
        return MyViewHolder(viewMvc)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
    ) {
        questions[position].let { holder.viewMvc.bindQuestion(it) }
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onQuestionClicked(question: Question?) {
        listener.onQuestionClicked(question)
    }
}
