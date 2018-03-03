package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view?.showHome()
    }

    override fun forwardTo(id: Int) {
        when (id) {
            R.id.navigation_home -> view?.showHome()

            R.id.navigation_looking -> view?.showLooking()

            R.id.navigation_person -> view?.showProfile()
        }
    }
}