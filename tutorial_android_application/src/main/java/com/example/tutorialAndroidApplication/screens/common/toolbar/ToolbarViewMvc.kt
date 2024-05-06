package com.example.tutorialAndroidApplication.screens.common.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.tutorialAndroidApplication.screens.common.views.BaseViewMvc
import com.example.tutorial_android_application.R

class ToolbarViewMvc(inflater: LayoutInflater, parent: ViewGroup) : BaseViewMvc() {
    interface NavigateUpClickListener {
        fun onNavigateUpClicked()
    }

    interface HamburgerClickListener {
        fun onHamburgerClicked()
    }

    private val txtTitle: TextView
    private val btnBack: ImageButton
    private val btnHamburger: ImageButton

    private var navigateUpClickListener1: NavigateUpClickListener? = null
    private var hamburgerClickListener1: HamburgerClickListener? = null

    init {
        setRootView(inflater.inflate(R.layout.layout_toolbar, parent, false))
        txtTitle = findViewById(R.id.txt_toolbar_title)
        btnHamburger = findViewById(R.id.btn_hamburger)
        btnHamburger.setOnClickListener {
            hamburgerClickListener1?.onHamburgerClicked()
        }
        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            navigateUpClickListener1?.onNavigateUpClicked()
        }
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun enableHamburgerButtonAndListen(hamburgerClickListener: HamburgerClickListener) {
        if (navigateUpClickListener1 != null) {
            throw RuntimeException("hamburger and up shouldn't be shown together")
        }
        hamburgerClickListener1 = hamburgerClickListener
        btnHamburger.visibility = View.VISIBLE
    }

    fun enableUpButtonAndListen(navigateUpClickListener: NavigateUpClickListener) {
        if (hamburgerClickListener1 != null) {
            throw RuntimeException("hamburger and up shouldn't be shown together")
        }
        navigateUpClickListener1 = navigateUpClickListener
        btnBack.visibility = View.VISIBLE
    }
}
