package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.ErrorLogin
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.main.MainActivity
import akhmedoff.usman.videoforvk.utils.vkApi
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {

    override lateinit var loginPresenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        loginPresenter = LoginPresenter(
            UserRepository(
                UserSettings.getUserSettings(applicationContext),
                vkApi
            )
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener { loginPresenter.login() }
    }

    override fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun errorPassword(error: ErrorLogin) {
    }

    override fun errorUsername(error: ErrorLogin) {
    }

    override fun onErrorLogin() {
        textView3.text = getString(R.string.error_login)
    }

    override fun initPresenter() = loginPresenter

    override fun getUsername() = username_et.text.toString().trim()

    override fun getPassword() = password_et.text.toString().trim()
}