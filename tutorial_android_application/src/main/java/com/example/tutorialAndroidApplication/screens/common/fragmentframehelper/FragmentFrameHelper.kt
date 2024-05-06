package com.example.tutorialAndroidApplication.screens.common.fragmentframehelper

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentFrameHelper(
    private val activity: Activity,
    private val fragmentFrameWrapper: FragmentFrameWrapper,
    private val fragmentManager: FragmentManager,
) {
    fun replaceFragment(newFragment: Fragment) {
        replaceFragment(newFragment, true, false)
    }

    fun replaceFragmentDontAddToBackstack(newFragment: Fragment) {
        replaceFragment(newFragment, false, false)
    }

    fun replaceFragmentAndClearBackstack(newFragment: Fragment) {
        replaceFragment(newFragment, false, true)
    }

    fun navigateUp() {
        // Some navigateUp calls can be "lost" if they happen after the state has been saved
        if (fragmentManager.isStateSaved) {
            return
        }
        val currentFragment = getCurrentFragment()

        if (fragmentManager.backStackEntryCount > 0) {
            // In a normal world, just popping back stack would be sufficient, but since android
            // is not normal, a call to popBackStack can leave the popped fragment on screen.
            // Therefore, we start with manual removal of the current fragment.
            // Description of the issue can be found here: https://stackoverflow.com/q/45278497/2463035
            removeCurrentFragment()
            if (fragmentManager.popBackStackImmediate()) {
                return // navigated "up" in fragments back-stack
            }
        }

        if (currentFragment is HierarchicalFragment) {
            val parentFragment = currentFragment.getHierarchicalParentFragment()
            if (parentFragment != null) {
                replaceFragment(parentFragment, false, true)
                return
            }
        }

        if (activity.onNavigateUp()) {
            return // navigated "up" to hierarchical parent activity
        }

        activity.onBackPressed() // no "up" navigation targets - just treat UP as back press
    }

    private fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(getFragmentFrameId())
    }

    private fun replaceFragment(
        newFragment: Fragment,
        addToBackStack: Boolean,
        clearBackStack: Boolean,
    ) {
        if (clearBackStack && fragmentManager.isStateSaved) {
            return
        }

        if (clearBackStack) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val ft = fragmentManager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(null)
        }

        ft.replace(getFragmentFrameId(), newFragment, null)

        if (fragmentManager.isStateSaved) {
            ft.commitAllowingStateLoss()
        } else {
            ft.commit()
        }
    }

    private fun removeCurrentFragment() {
        val ft = fragmentManager.beginTransaction()
        ft.remove(getCurrentFragment()!!)
        ft.commit()

        // not sure it is needed; will keep it as a reminder to myself if there will be problems
        // mFragmentManager.executePendingTransactions();
    }

    private fun getFragmentFrameId(): Int {
        return fragmentFrameWrapper.getFragmentFrame().id
    }
}
