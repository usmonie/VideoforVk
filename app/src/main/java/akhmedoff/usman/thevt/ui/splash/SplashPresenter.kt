package akhmedoff.usman.thevt.ui.splash

import akhmedoff.usman.data.repository.UserRepository

class SplashPresenter(
        override var view: SplashContract.View?,
        private val repository: UserRepository
) :
        SplashContract.Presenter {

    override fun onCreate() {
        when {
            repository.isLogged -> view?.showMain()

            else -> view?.showLogin()
        }
    }

    override fun onDestroy() {
        view = null
    }
}