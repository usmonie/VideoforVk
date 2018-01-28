package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.ErrorLogin
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import android.arch.lifecycle.Observer

class LoginPresenter(private val repository: UserRepository) : BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {

    override fun login() {
        view?.let {
            if (it.getUsername().isBlank()) {
                it.errorUsername(ErrorLogin.EMPTY_USERNAME)
                return
            }
            if (it.getPassword().isBlank()) {
                it.errorPassword(ErrorLogin.EMPTY_PASSWORD)
                return
            }

            repository.auth(it.getUsername(), it.getPassword()).observe(it, Observer { response ->
                when {
                    response?.response != null && response.response.isSuccessfull -> {
                        response.response.accessToken?.let { token -> repository.saveToken(token) }
                        it.startMain()
                    }
                    else -> it.onErrorLogin()
                }
            })
        }

    }
}