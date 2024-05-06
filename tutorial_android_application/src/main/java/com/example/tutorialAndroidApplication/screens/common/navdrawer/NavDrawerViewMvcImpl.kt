package com.example.tutorialAndroidApplication.screens.common.navdrawer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tutorialAndroidApplication.screens.common.views.BaseObservableViewMvc
import com.example.tutorial_android_application.R
import com.google.android.material.navigation.NavigationView

class NavDrawerViewMvcImpl(inflater: LayoutInflater, parent: ViewGroup?) :
    BaseObservableViewMvc<NavDrawerViewMvc.Listener>(), NavDrawerViewMvc {
    private val drawerLayout: DrawerLayout
    private val frameLayout: FrameLayout
    private val navigationView: NavigationView

    init {
        setRootView(inflater.inflate(R.layout.layout_drawer, parent, false))
        drawerLayout = findViewById(R.id.drawer_layout)
        frameLayout = findViewById(R.id.frame_content)
        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            if (item.itemId == R.id.drawer_menu_questions_list) {
                for (listener in getListeners()) {
                    listener.onQuestionsListClicked()
                }
            }
            false
        }
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun isDrawerOpen(): Boolean {
        return drawerLayout.isDrawerOpen(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawers()
    }

    override fun getFragmentFrame(): FrameLayout {
        return frameLayout
    }
}
