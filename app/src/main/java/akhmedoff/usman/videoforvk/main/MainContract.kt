package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.base.BaseContract
import android.support.v4.app.Fragment

interface MainContract {

    interface View : BaseContract.View {

        var mainPresenter: Presenter

        fun showHome()

        fun showProfile()

        fun showFavourites()
    }

    interface Presenter : BaseContract.Presenter<MainContract.View> {

        fun navigate(id: Int)

        fun updateCurrentFragment(it: Fragment)
    }
}