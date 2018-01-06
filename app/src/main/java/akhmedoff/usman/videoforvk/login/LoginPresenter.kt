package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.BuildConfig
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import android.net.Uri
import java.util.regex.Pattern

class LoginPresenter(private val repository: UserRepository) : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    companion object {
        const val AUTH_URL = "https://oauth.vk.com/authorize?" +
                "client_id=" + BuildConfig.VK_APP_ID +
                "&redirect_uri=https://oauth.vk.com/blank.html" +
                "&scope=friends,video,wall,offline,groups,status" +
                "&response_type=token&v=5.69"
    }

    override fun login() {
        view.startLogin(AUTH_URL)
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