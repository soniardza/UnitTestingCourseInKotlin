package com.example.tutorialAndroidApplication.screens.common.views

import java.util.Collections
import java.util.HashSet

abstract class BaseObservableViewMvc<ListenerType> : BaseViewMvc(), ObservableViewMvc<ListenerType> {
    private val listeners: MutableSet<ListenerType> = HashSet()

    final override fun registerListener(listener: ListenerType) {
        listeners.add(listener)
    }

    final override fun unregisterListener(listener: ListenerType) {
        listeners.remove(listener)
    }

    protected fun getListeners(): Set<ListenerType> {
        return Collections.unmodifiableSet(listeners)
    }
}
