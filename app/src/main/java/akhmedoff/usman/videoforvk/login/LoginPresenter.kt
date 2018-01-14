package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.ErrorLogin
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import android.arch.lifecycle.Observer
import android.net.Uri
import java.util.regex.Pattern

class LoginPresenter(private val repository: UserRepository) : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun login() {
        if (view.getUsername().isBlank()) {
            view.errorUsername(ErrorLogin.EMPTY_USERNAME)
            return
        }
        if (view.getPassword().isBlank()) {
            view.errorPassword(ErrorLogin.EMPTY_PASSWORD)
            return
        }

        repository.auth(view.getUsername(), view.getPassword()).observe(view, Observer {
            when {
                it != null && it.isSuccessfull && it.response != null && it.response.isSuccessfull -> {
                    it.response.accessToken?.let { token -> repository.saveToken(token) }
                    view.startMain()

                }
                else -> view.onErrorLogin()
            }
        })
    }

    override fun onLoggined(response: Uri) {
        val token = extractPattern(response.toString(), "access_token=(.*?)&")
        when {
            !token.isNullOrBlank() -> {
                repository.saveToken(token!!)
                view.startMain()
            }
            else -> view.onErrorLogin()
        }
    }

    private fun extractPattern(string: String, pattern: String): String? {
        val p = Pattern.compile(pattern)
        val m = p.matcher(string)
        return when {
            !m.find() -> null
            else -> m.toMatchResult().group(1)
        }
    }
}