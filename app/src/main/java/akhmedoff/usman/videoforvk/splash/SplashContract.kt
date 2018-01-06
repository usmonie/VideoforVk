package akhmedoff.usman.videoforvk.splash

import akhmedoff.usman.videoforvk.base.BaseContract

interface SplashContract {

    interface View : BaseContract.View {
        var splashPresenter: Presenter

        fun showMain()

        fun showLogin()
    }

    interface Presenter : BaseContract.Presenter<View>
}