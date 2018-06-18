package akhmedoff.usman.videoforvk.ui.splash

import akhmedoff.usman.videoforvk.ui.base.BaseContract

interface SplashContract {

    interface View : BaseContract.View {
        var splashPresenter: Presenter

        fun showMain()

        fun showLogin()

    }

    interface Presenter : BaseContract.Presenter<View>
}