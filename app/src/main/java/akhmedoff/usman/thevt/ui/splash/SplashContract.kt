package akhmedoff.usman.thevt.ui.splash

interface SplashContract {

    interface View {
        var splashPresenter: Presenter

        fun showMain()

        fun showLogin()

    }

    interface Presenter {

        var view: View?

        fun onCreate()

        fun onDestroy()
    }
}