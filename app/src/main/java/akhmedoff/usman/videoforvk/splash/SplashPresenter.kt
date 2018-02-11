package akhmedoff.usman.videoforvk.splash

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository

class SplashPresenter(private val repository: UserRepository) :
    BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    override fun onPresenterCreated() {
        super.onPresenterCreated()
        when {
            repository.isLogged -> view?.showMain()
            else -> view?.showLogin()
        }
    }
}