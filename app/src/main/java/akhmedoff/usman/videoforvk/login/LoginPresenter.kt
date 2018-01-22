package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.ErrorLogin
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import android.arch.lifecycle.Observer

class LoginPresenter(private val repository: UserRepository) : BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {

    override fun login() {
        view?.let {
            if (view!!.getUsername().isBlank()) {
                view?.errorUsername(ErrorLogin.EMPTY_USERNAME)
                return
            }
            if (view!!.getPassword().isBlank()) {
                view?.errorPassword(ErrorLogin.EMPTY_PASSWORD)
                return
            }

            repository.auth(view!!.getUsername(), view!!.getPassword()).observe(view!!, Observer {
                when {
                    it != null && it.isSuccessfull && it.response != null && it.response.isSuccessfull -> {
                        it.response.accessToken?.let { token -> repository.saveToken(token) }
                        view!!.startMain()
                    }
                    else -> view!!.onErrorLogin()
                }
            })
        }

    }
}