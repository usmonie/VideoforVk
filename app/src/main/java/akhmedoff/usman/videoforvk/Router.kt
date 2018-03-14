package akhmedoff.usman.videoforvk

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction


object Router {

    fun showFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        fragmentTag: String
    ) {
        if (fragment.isAdded) {
            if (addToBackStack) {
                fragmentManager.beginTransaction()
                    .show(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
            } else {
                fragmentManager.beginTransaction()
                    .show(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
        } else {
            addFragment(fragmentManager, fragment, fragmentTag)
        }

    }

    fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
            .hide(fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        fragmentTag: String?
    ) {
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    fun addFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
            .add(R.id.container, fragment, tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    fun removeFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
            .remove(fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

}