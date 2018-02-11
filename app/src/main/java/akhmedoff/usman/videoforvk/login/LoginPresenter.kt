package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.model.Auth
import akhmedoff.usman.videoforvk.utils.gson
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    private val repository: UserRepository
) : BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {

    private lateinit var captchaSid: String

    override fun login() {
        val username = view?.getUsername()
        val password = view?.getPassword()
        if (username.isNullOrBlank()) {
            view?.userNameIsShort()
            return
        }
        if (password.isNullOrBlank()) {
            view?.passwordIsShort()
            return
        }

        auth(username = username!!, password = password!!)
    }

    override fun enterCode(code: String) {
        view?.let {
            if (code.trim().isEmpty()) return
            auth(it.getUsername(), it.getPassword(), code = code)

        }
    }

    override fun enterCaptcha(captchaKey: String) {
        view?.let {
            if (captchaKey.trim().isEmpty()) return
            auth(it.getUsername(), it.getPassword(), captchaSid, captchaKey, null)

        }
    }

    private fun auth(
        username: String,
        password: String,
        captchaSid: String? = null,
        captchaKey: String? = null,
        code: String? = null
    ) {
        view?.let {
            repository
                .auth(username, password, captchaSid, captchaKey, code)
                .enqueue(object : Callback<Auth> {
                    override fun onFailure(call: Call<Auth>?, t: Throwable?) {
                        Log.e("failed", t.toString())
                    }

                    override fun onResponse(call: Call<Auth>, response: Response<Auth>) {

                        response.body()?.accessToken?.let { token ->
                            repository.saveToken(
                                token
                            )
                            it.startMain()
                        }

                        response.errorBody()?.let { errorConvert(it) }

                    }

                })
        }

    }

    private fun errorConvert(response: ResponseBody) {
        val auth = gson.fromJson<Auth>(response.string(), Auth::class.java)

        view?.let {
            when (auth.error) {
                Error.ERROR_LOGIN -> it.onErrorLogin()
                Error.NEED_CAPTCHA -> {
                    captchaSid = auth.captchaSid!!
                    it.captcha(auth.captchaImg!!)
                }
                Error.NEED_VALIDATION ->
                    when (auth.validationType) {
                        null -> it.onErrorLogin()
                        else -> it.validateTwoFactoryAuthorization(auth.phoneMask)
                    }
                else -> it.onErrorLogin()
            }
        }
    }

    override fun error(error: Error, message: String) {
        view?.let {
            when (error) {
                Error.ERROR_LOGIN -> {
                    it.errorUsername()
                    it.errorPassword()
                }

                Error.NEED_CAPTCHA -> {
                }

                Error.NEED_VALIDATION -> {
                }
                else -> {
                }
            }
        }
    }
}
