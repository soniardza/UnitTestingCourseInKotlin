package com.example.tutorialAndroidApplication.screens.common.navdrawer

import android.widget.FrameLayout
import com.example.tutorialAndroidApplication.screens.common.views.ObservableViewMvc

interface NavDrawerViewMvc : ObservableViewMvc<NavDrawerViewMvc.Listener> {
    interface Listener {
        fun onQuestionsListClicked()
    }

    fun getFragmentFrame(): FrameLayout

    fun isDrawerOpen(): Boolean

    fun openDrawer()

    fun closeDrawer()
}
