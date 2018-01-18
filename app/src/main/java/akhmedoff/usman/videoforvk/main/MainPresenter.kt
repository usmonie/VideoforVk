package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BasePresenter

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun navigate(id: Int) = when (id) {
        R.id.navigation_home -> {
            view.showHome()
            true
        }
        R.id.navigation_favourite -> {
            view.showFavourites()
            true
        }
        R.id.navigation_profile -> {
            view.showProfile()
            true
        }
        else -> false
    }
}