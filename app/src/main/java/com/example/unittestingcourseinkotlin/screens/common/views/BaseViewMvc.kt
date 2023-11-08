package com.example.unittestingcourseinkotlin.screens.common.views

import android.content.Context
import android.view.View
import androidx.annotation.StringRes

abstract class BaseViewMvc : ViewMvc {

    var mRootView: View? = null

    override fun getRootView(): View? {
        return mRootView
    }

    protected fun setRootView(rootView: View) {
        mRootView = rootView
    }

    protected inline fun <reified T : View> findViewById(id: Int): T? {
        return mRootView?.findViewById(id)
    }

    protected fun getContext(): Context? {
        return mRootView?.context
    }

    protected fun getString(@StringRes id: Int): String {
        return getContext()?.getString(id) ?: ""
    }
}
