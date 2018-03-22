package akhmedoff.usman.videoforvk

import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import android.view.View


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
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment, fragmentTag)
                .addToBackStack(null)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment, fragmentTag)
                .commit()
        }
    }

    fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        fragmentTag: String?,
        sharedElement: View
    ) {

        // 2. Shared Elements Transition
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(
            ChangeClipBounds()
        )
        enterTransitionSet.addTransition(
            ChangeTransform()
        )
        enterTransitionSet.addTransition(
            ChangeBounds()
        )
        enterTransitionSet.duration = 600
        fragment.sharedElementEnterTransition = enterTransitionSet

        // 3. Enter Transition for New Fragment
        val explodeFade = Explode()
        explodeFade.duration = 200
        fragment.enterTransition = explodeFade

        if (addToBackStack) {
            fragmentManager.beginTransaction()
                .addSharedElement(
                    sharedElement,
                    ViewCompat.getTransitionName(sharedElement)
                )
                .replace(R.id.container, fragment, fragmentTag)
                .addToBackStack(null)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .addSharedElement(
                    sharedElement,
                    ViewCompat.getTransitionName(sharedElement)
                )
                .replace(R.id.container, fragment, fragmentTag)
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