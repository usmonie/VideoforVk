package akhmedoff.usman.videoforvk.splash

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