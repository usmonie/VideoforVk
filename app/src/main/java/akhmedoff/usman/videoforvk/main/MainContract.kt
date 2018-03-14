package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {
        var mainPresenter: Presenter

        fun showHome()

        fun showProfile()

        fun showLooking()

        fun showSettings()

        fun hidePrevious()

        fun showLastFragment()

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onCreate()

        fun onRecreate()

        fun forwardTo(id: Int)
    }
}