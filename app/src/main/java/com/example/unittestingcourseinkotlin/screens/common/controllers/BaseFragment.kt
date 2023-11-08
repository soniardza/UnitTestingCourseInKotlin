package com.example.unittestingcourseinkotlin.screens.common.controllers

import androidx.fragment.app.Fragment
import com.example.unittestingcourseinkotlin.common.CustomApplication
import com.example.unittestingcourseinkotlin.common.dependencyinjection.ControllerCompositionRoot

open class BaseFragment : Fragment() {
    private var mControllerCompositionRoot: ControllerCompositionRoot? = null
    protected val compositionRoot: ControllerCompositionRoot
        get() {
            if (mControllerCompositionRoot == null) {
                mControllerCompositionRoot = ControllerCompositionRoot(
                    (requireActivity().application as CustomApplication).compositionRoot!!,
                    requireActivity(),
                )
            }
            return mControllerCompositionRoot!!
        }
}
