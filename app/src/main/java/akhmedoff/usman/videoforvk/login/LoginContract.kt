package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.ErrorLogin
import akhmedoff.usman.videoforvk.base.BaseContract
import android.net.Uri

interface LoginContract {

    interface View : BaseContract.View {
        var loginPresenter: Presenter

        fun startMain()

        fun onErrorLogin()

        fun getUsername(): String

        fun getPassword(): String

        fun errorUsername(error: ErrorLogin)

        fun errorPassword(error: ErrorLogin)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun login()

        fun onLoggined(response: Uri)
    }
}