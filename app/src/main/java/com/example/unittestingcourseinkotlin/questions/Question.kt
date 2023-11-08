package com.example.unittestingcourseinkotlin.questions

import java.util.Objects

class Question(val id: String, val title: String) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val question = o as Question
        return id == question.id && title == question.title
    }

    override fun hashCode(): Int {
        return Objects.hash(id, title)
    }
}
