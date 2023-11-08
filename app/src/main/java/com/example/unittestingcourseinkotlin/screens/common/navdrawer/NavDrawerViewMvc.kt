package com.example.unittestingcourseinkotlin.screens.common.navdrawer

import android.widget.FrameLayout
import com.example.unittestingcourseinkotlin.screens.common.views.ObservableViewMvc

interface NavDrawerViewMvc : ObservableViewMvc<NavDrawerViewMvc.Listener?> {
    interface Listener {
        fun onQuestionsListClicked()
    }

    val fragmentFrame: FrameLayout?
    val isDrawerOpen: Boolean

    fun openDrawer()
    fun closeDrawer()
}
