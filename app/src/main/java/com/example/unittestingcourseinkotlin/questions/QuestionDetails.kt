package com.example.unittestingcourseinkotlin.questions

import java.util.Objects

class QuestionDetails(val id: String, val title: String, val body: String) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as QuestionDetails
        return id == that.id && title == that.title && body == that.body
    }

    override fun hashCode(): Int {
        return Objects.hash(id, title, body)
    }
}
