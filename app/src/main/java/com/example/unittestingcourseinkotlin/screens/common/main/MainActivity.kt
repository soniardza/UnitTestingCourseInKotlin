package com.example.unittestingcourseinkotlin.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.example.unittestingcourseinkotlin.screens.common.controllers.BackPressDispatcher
import com.example.unittestingcourseinkotlin.screens.common.controllers.BackPressedListener
import com.example.unittestingcourseinkotlin.screens.common.controllers.BaseActivity
import com.example.unittestingcourseinkotlin.screens.common.fragmentframehelper.FragmentFrameWrapper
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerHelper
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerViewMvc
import com.example.unittestingcourseinkotlin.screens.common.screensnavigator.ScreensNavigator

class MainActivity :
    BaseActivity(),
    BackPressDispatcher,
    FragmentFrameWrapper,
    NavDrawerViewMvc.Listener,
    NavDrawerHelper {
    private val mBackPressedListeners: MutableSet<BackPressedListener?> = HashSet()
    private var mScreensNavigator: ScreensNavigator? = null
    private var mViewMvc: NavDrawerViewMvc? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScreensNavigator = compositionRoot.screensNavigator
        mViewMvc = compositionRoot.viewMvcFactory.getNavDrawerViewMvc(null)
        setContentView(mViewMvc!!.getRootView())
        if (savedInstanceState == null) {
            mScreensNavigator!!.toQuestionsList()
        }
    }

    override fun onStart() {
        super.onStart()
        mViewMvc!!.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mViewMvc!!.unregisterListener(this)
    }

    override fun onQuestionsListClicked() {
        mScreensNavigator!!.toQuestionsList()
    }

    override fun registerListener(listener: BackPressedListener?) {
        mBackPressedListeners.add(listener)
    }

    override fun unregisterListener(listener: BackPressedListener?) {
        mBackPressedListeners.remove(listener)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        var isBackPressConsumedByAnyListener = false
        for (listener in mBackPressedListeners) {
            if (listener!!.onBackPressed()) {
                isBackPressConsumedByAnyListener = true
            }
        }
        if (!isBackPressConsumedByAnyListener) {
            super.onBackPressed()
        }
    }

    override val fragmentFrame: FrameLayout?
        get() = mViewMvc!!.fragmentFrame

    override fun openDrawer() {
        mViewMvc!!.openDrawer()
    }

    override fun closeDrawer() {
        mViewMvc!!.closeDrawer()
    }

    override val isDrawerOpen: Boolean
        get() = mViewMvc!!.isDrawerOpen
}
