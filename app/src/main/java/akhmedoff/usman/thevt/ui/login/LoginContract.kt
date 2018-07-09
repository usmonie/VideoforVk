package akhmedoff.usman.thevt.ui.login


interface LoginContract {

    interface View {
        var loginPresenter: Presenter

        fun startMain()

        fun onErrorLogin()

        fun getUsername(): String

        fun getPassword(): String

        fun errorUsername()

        fun errorPassword()

        fun userNameIsShort()

        fun passwordIsShort()

        fun validateTwoFactoryAuthorization(phoneMask: String?)

        fun captcha(captchaUrl: String)

        fun showProgress()

        fun hideProgress()

        fun setButtonEnabled(enabled: Boolean)

        fun editTextEditable(editable: Boolean)

        fun isDialogShows(): Boolean

        fun showDialogLoading()

        fun hideDialogLoading()

        fun hideDialogs()

        fun showCodeError()

        fun hideKeyboard()

    }

    interface Presenter {

        var view: View?

        fun login()

        fun enterCaptcha(captchaKey: String)

        fun enterCode(code: String)

    }
}