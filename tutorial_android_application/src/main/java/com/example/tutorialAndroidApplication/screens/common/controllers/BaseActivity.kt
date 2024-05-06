package com.example.tutorialAndroidApplication.screens.common.controllers

import androidx.appcompat.app.AppCompatActivity
import com.example.tutorialAndroidApplication.common.CustomApplication
import com.example.tutorialAndroidApplication.common.dependencyinjection.ControllerCompositionRoot

open class BaseActivity : AppCompatActivity() {
    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot =
                ControllerCompositionRoot(
                    (application as CustomApplication).compositionRoot,
                    this,
                )
        }
        return controllerCompositionRoot!!
    }
}
