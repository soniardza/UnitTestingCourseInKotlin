package com.example.unittestingcourseinkotlin.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.example.unittestingcourseinkotlin.common.CustomApplication
import com.example.unittestingcourseinkotlin.common.dependencyinjection.ControllerCompositionRoot

open class BaseActivity : AppCompatActivity() {
    private var mControllerCompositionRoot: ControllerCompositionRoot? = null
    protected val compositionRoot: ControllerCompositionRoot
        get() {
            if (mControllerCompositionRoot == null) {
                mControllerCompositionRoot = ControllerCompositionRoot(
                    (application as CustomApplication).compositionRoot!!,
                    this,
                )
            }
            return mControllerCompositionRoot!!
        }
}
