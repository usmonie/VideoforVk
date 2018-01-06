package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.base.BaseContract
import android.net.Uri

interface LoginContract {

    interface View : BaseContract.View {
        var loginPresenter: Presenter

        fun startLogin(url: String)

        fun startMain()

        fun onErrorLogin()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun login()

        fun onLoggined(response: Uri)
    }
}