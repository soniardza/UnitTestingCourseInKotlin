package com.example.tutorialAndroidApplication.screens.common.controllers

import androidx.fragment.app.Fragment
import com.example.tutorialAndroidApplication.common.CustomApplication
import com.example.tutorialAndroidApplication.common.dependencyinjection.ControllerCompositionRoot

open class BaseFragment : Fragment() {
    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot =
                ControllerCompositionRoot(
                    (requireActivity().application as CustomApplication).compositionRoot,
                    requireActivity(),
                )
        }
        return controllerCompositionRoot!!
    }
}
