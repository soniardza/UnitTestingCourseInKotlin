package com.example.unittestingcourseinkotlin.screens.common.views

import java.util.Collections

abstract class BaseObservableViewMvc<ListenerType> :
    BaseViewMvc(),
    ObservableViewMvc<ListenerType> {
    private val mListeners: MutableSet<ListenerType> = HashSet()
    override fun registerListener(listener: ListenerType) {
        mListeners.add(listener)
    }

    override fun unregisterListener(listener: ListenerType) {
        mListeners.remove(listener)
    }

    protected val listeners: Set<ListenerType>
        get() = Collections.unmodifiableSet(mListeners)
}
