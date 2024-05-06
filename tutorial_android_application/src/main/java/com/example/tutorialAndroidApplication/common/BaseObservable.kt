package com.example.tutorialAndroidApplication.common

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

abstract class BaseObservable<LISTENER_CLASS> {
    // thread-safe set of listeners
    private val listeners =
        Collections.newSetFromMap(
            ConcurrentHashMap<LISTENER_CLASS, Boolean>(1),
        )

    fun registerListener(listener: LISTENER_CLASS) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER_CLASS) {
        listeners.remove(listener)
    }

    protected val getListeners: kotlin.collections.Set<LISTENER_CLASS>
        protected get() = Collections.unmodifiableSet(listeners)
}
