package com.example.tutorialAndroidApplication.screens.questionslist.questionslistitem

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.tutorialAndroidApplication.questions.Question
import com.example.tutorialAndroidApplication.screens.common.views.BaseObservableViewMvc
import com.example.tutorial_android_application.R

class QuestionsListItemViewMvcImpl(inflater: LayoutInflater, parent: ViewGroup?) :
    BaseObservableViewMvc<QuestionsListItemViewMvc.Listener>(), QuestionsListItemViewMvc {
    private val txtTitle: TextView

    private var question: Question? = null

    init {
        setRootView(inflater.inflate(R.layout.layout_question_list_item, parent, false))

        txtTitle = findViewById(R.id.txt_title)
        getRootView().setOnClickListener {
            for (listener in getListeners()) {
                listener.onQuestionClicked(question!!)
            }
        }
    }

    override fun bindQuestion(question: Question?) {
        this.question = question
        if (question != null) {
            txtTitle.text = question.title
        }
    }
}
