package akhmedoff.usman.videoforvk.splash

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent

class SplashPresenter(private val repository: UserRepository) :
    BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view?.let { view ->
            when {
                repository.isLogged -> view.showMain()
                else -> view.showLogin()
            }
        }
    }
}