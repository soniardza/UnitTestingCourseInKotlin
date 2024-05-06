package com.example.tutorialAndroidApplication.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.example.tutorialAndroidApplication.screens.common.controllers.BackPressDispatcher
import com.example.tutorialAndroidApplication.screens.common.controllers.BackPressedListener
import com.example.tutorialAndroidApplication.screens.common.controllers.BaseActivity
import com.example.tutorialAndroidApplication.screens.common.fragmentframehelper.FragmentFrameWrapper
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerHelper
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerViewMvc
import com.example.tutorialAndroidApplication.screens.common.screensnavidator.ScreensNavigator

class MainActivity :
    BaseActivity(),
    BackPressDispatcher,
    FragmentFrameWrapper,
    NavDrawerViewMvc.Listener,
    NavDrawerHelper {
    private val backPressedListeners: MutableSet<BackPressedListener> = HashSet()
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var viewMvc: NavDrawerViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screensNavigator = getCompositionRoot().getScreensNavigator()
        viewMvc = getCompositionRoot().getViewMvcFactory().getNavDrawerViewMvc(null)
        setContentView(viewMvc!!.getRootView())
        if (savedInstanceState == null) {
            screensNavigator!!.toQuestionsList()
        }
    }

    override fun onStart() {
        super.onStart()
        viewMvc!!.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc!!.unregisterListener(this)
    }

    override fun onQuestionsListClicked() {
        screensNavigator!!.toQuestionsList()
    }

    override fun registerListener(listener: BackPressedListener) {
        backPressedListeners.add(listener)
    }

    override fun unregisterListener(listener: BackPressedListener) {
        backPressedListeners.remove(listener)
    }

    override fun onBackPressed() {
        var isBackPressConsumedByAnyListener = false
        for (listener in backPressedListeners) {
            if (listener.onBackPressed()) {
                isBackPressConsumedByAnyListener = true
            }
        }
        if (!isBackPressConsumedByAnyListener) {
            super.onBackPressed()
        }
    }

    override fun getFragmentFrame(): FrameLayout {
        return viewMvc!!.getFragmentFrame()
    }

    override fun openDrawer() {
        viewMvc!!.openDrawer()
    }

    override fun closeDrawer() {
        viewMvc!!.closeDrawer()
    }

    override fun isDrawerOpen(): Boolean {
        return viewMvc!!.isDrawerOpen()
    }
}
