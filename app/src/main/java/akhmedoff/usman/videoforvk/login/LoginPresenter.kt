package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Auth
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.utils.gson
import akhmedoff.usman.videoforvk.base.BasePresenter
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

        view?.let { view ->
            if (view.isDialogShows()) view.showDialogLoading()
            else view.showProgress()

            view.setButtonEnabled(enabled = false)
            view.editTextEditable(editable = false)

            repository
                .auth(username, password, captchaSid, captchaKey, code)
                .enqueue(object : Callback<Auth> {
                    override fun onFailure(call: Call<Auth>?, t: Throwable?) {
                        Log.e("failed", t?.toString())
                        view.hideProgress()
                        view.setButtonEnabled(enabled = true)
                        view.editTextEditable(editable = true)
                        view.onErrorLogin()

                        if (view.isDialogShows()) view.hideDialogLoading()
                    }

                    override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                        view.hideProgress()
                        view.setButtonEnabled(enabled = true)
                        view.editTextEditable(editable = true)

                        response.body()?.let { auth ->
                            if (auth.isSuccessful) {
                                repository.saveToken(auth.accessToken!!)
                                repository.saveCurrentUser(auth.userId!!)
                                view.startMain()
                            } else {
                                view.onErrorLogin()
                            }
                        }

                        if (view.isDialogShows()) view.hideDialogLoading()

                        response.errorBody()?.let { errorConvert(it) }
                    }
                })
        }

    }

    private fun errorConvert(response: ResponseBody) {
        val auth = gson.fromJson<Auth>(response.string(), Auth::class.java)

            when (auth.error) {
                Error.ERROR_LOGIN -> view?.onErrorLogin()

                Error.NEED_CAPTCHA -> {
                    captchaSid = auth.captchaSid!!
                    view?.captcha(auth.captchaImg!!)
                }

                Error.NEED_VALIDATION ->
                    when (auth.validationType) {
                        null -> view?.onErrorLogin()
                        else -> view?.validateTwoFactoryAuthorization(auth.phoneMask)
                    }

                Error.INVALID_CODE -> view?.showCodeError()

                else -> view?.onErrorLogin()
            }
    }
}
