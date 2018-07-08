package akhmedoff.usman.videoforvk

import android.app.Activity
import android.content.Intent
import android.support.transition.*
import android.support.transition.TransitionSet.ORDERING_TOGETHER
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.view.View


object Router {

    fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
                .hide(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    fun startActivityWithTransition(activity: Activity, intent: Intent, view: View) =
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair(view, view.transitionName)).toBundle())

    fun replaceFragment(
            fragmentManager: FragmentManager,
            prevFragment: Fragment? = null,
            detailFragment: Fragment,
            addToBackStack: Boolean = false,
            fragmentTag: String?,
            sharedElement: View
    ) {
        val transaction = fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, detailFragment, fragmentTag)
                .setTransition(TRANSIT_FRAGMENT_FADE)

        if (addToBackStack) transaction.addToBackStack(fragmentTag)

        prevFragment?.exitTransition = Explode()

        val enterTransitionSet = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeClipBounds())
                .addTransition(ChangeImageTransform())
        enterTransitionSet.ordering = ORDERING_TOGETHER

        detailFragment.sharedElementEnterTransition = enterTransitionSet

        detailFragment.enterTransition = Slide()

        val returnTransitionSet = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeClipBounds())
                .addTransition(ChangeImageTransform())
        enterTransitionSet.ordering = ORDERING_TOGETHER
        detailFragment.sharedElementReturnTransition = returnTransitionSet
        detailFragment.returnTransition = null

        ViewCompat.getTransitionName(sharedElement)?.let {
            transaction.addSharedElement(
                    sharedElement,
                    it
            )
        }

        transaction.commit()
    }

    fun replaceFragment(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            addToBackStack: Boolean = false,
            fragmentTag: String?
    ) {
        val transaction = fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment, fragmentTag)

        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag)
        }

        transaction.setTransition(TRANSIT_FRAGMENT_OPEN)

        transaction.commit()
    }

}