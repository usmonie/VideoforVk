package akhmedoff.usman.videoforvk.splash

import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent

class SplashPresenter(private val repository: UserRepository) :
    BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = when {
        repository.isLogged -> view?.showMain()

        else -> view?.showLogin()
    }
}