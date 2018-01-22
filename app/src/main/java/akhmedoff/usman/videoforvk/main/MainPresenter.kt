package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {
    var itemNavigationId: Int = R.id.navigation_home

    override fun navigate(id: Int) {
        itemNavigationId = id
        when (id) {
            R.id.navigation_home -> view?.showHome()
            R.id.navigation_favourite -> view?.showFavourites()
            R.id.navigation_profile -> view?.showProfile()
        }
    }

    override fun updateCurrentFragment(it: Fragment) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        navigate(itemNavigationId)
    }
}