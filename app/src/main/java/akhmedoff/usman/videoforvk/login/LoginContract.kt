package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BaseContract

interface LoginContract {

    interface View : BaseContract.View {
        var loginPresenter: Presenter

        fun startMain()

        fun onErrorLogin()

        fun getUsername(): String

        fun getPassword(): String

        fun errorUsername()

        fun errorPassword()

        fun userNameIsShort()

        fun passwordIsShort()

        fun validate()

        fun validateTwoFactoryAuthorization(phoneMask: String?)

        fun captcha(captchaUrl: String)

        fun getCaptchaKey(): String

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun login()

        fun enterCaptcha(captchaKey: String)

        fun error(error: Error, message: String)

        fun enterCode(code: String)
    }
}