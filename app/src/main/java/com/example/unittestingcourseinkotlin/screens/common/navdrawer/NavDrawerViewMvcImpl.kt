package com.example.unittestingcourseinkotlin.screens.common.navdrawer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.unittestingcourseinkotlin.R
import com.example.unittestingcourseinkotlin.screens.common.views.BaseObservableViewMvc
import com.google.android.material.navigation.NavigationView

class NavDrawerViewMvcImpl(inflater: LayoutInflater, parent: ViewGroup?) :
    BaseObservableViewMvc<NavDrawerViewMvc.Listener?>(),
    NavDrawerViewMvc {
    private val mDrawerLayout: DrawerLayout?
    override val fragmentFrame: FrameLayout?
    private val mNavigationView: NavigationView?

    init {
        setRootView(inflater.inflate(R.layout.layout_drawer, parent, false))
        mDrawerLayout = findViewById(R.id.drawer_layout)
        fragmentFrame = findViewById(R.id.frame_content)
        mNavigationView = findViewById(R.id.nav_view)
        mNavigationView!!.setNavigationItemSelectedListener { item ->
            mDrawerLayout!!.closeDrawers()
            if (item.itemId == R.id.drawer_menu_questions_list) {
                for (listener in listeners) {
                    listener?.onQuestionsListClicked()
                }
            }
            false
        }
    }

    override fun openDrawer() {
        mDrawerLayout!!.openDrawer(GravityCompat.START)
    }

    override val isDrawerOpen: Boolean
        get() = mDrawerLayout!!.isDrawerOpen(GravityCompat.START)

    override fun closeDrawer() {
        mDrawerLayout!!.closeDrawers()
    }
}
