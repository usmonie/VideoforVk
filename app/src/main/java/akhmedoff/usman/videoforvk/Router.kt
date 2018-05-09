package akhmedoff.usman.videoforvk

import android.support.transition.*
import android.support.transition.TransitionSet.ORDERING_TOGETHER
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import android.view.View


object Router {

    private const val FADE_DEFAULT_TIME = 300L
    private const val MOVE_DEFAULT_TIME = 500L

    fun showFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        fragmentTag: String
    ) {
        if (fragment.isAdded) {
            val transaction = fragmentManager.beginTransaction()
                .show(fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }

            transaction.commit()
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
        prevFragment: Fragment? = null,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        fragmentTag: String?,
        sharedElement: View? = null
    ) {
        val transaction =
            fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment, fragmentTag)

        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag)
        }


        sharedElement?.let {
            val exitFade = Fade()
            prevFragment?.exitTransition = exitFade

            val enterTransitionSet: Transition = TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeTransform())
                    .addTransition(ChangeClipBounds())
                    .addTransition(ChangeImageTransform())

            enterTransitionSet.duration = 375L

            fragment.sharedElementEnterTransition = enterTransitionSet

            val enterFade = Fade()
            fragment.enterTransition = enterFade

            transaction
                .addSharedElement(
                    it,
                    ViewCompat.getTransitionName(it)
                )
        }

        transaction.commit()
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