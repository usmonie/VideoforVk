package akhmedoff.usman.thevt

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.transition.*
import androidx.transition.TransitionSet.ORDERING_TOGETHER


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

        val slide = Slide()
        prevFragment?.exitTransition = slide

        val enterTransitionSet = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeClipBounds())
                .addTransition(ChangeImageTransform())

        enterTransitionSet.ordering = ORDERING_TOGETHER

        detailFragment.sharedElementEnterTransition = enterTransitionSet
        slide.slideEdge = 80
        detailFragment.enterTransition = slide

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