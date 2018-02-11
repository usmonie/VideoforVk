package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.videoforvk.CaptchaDialog
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.main.MainActivity
import akhmedoff.usman.videoforvk.utils.vkApi
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {
    override fun getCaptchaKey(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    override fun errorPassword() {
        password_input_layout.error = resources.getText(R.string.password_error)
        password_input_layout.isErrorEnabled = true
    }

    override fun errorUsername() {
        username_input_layout.error = resources.getText(R.string.username_error)
        username_input_layout.isErrorEnabled = true
    }

    override fun userNameIsShort() {
        username_input_layout.error = resources.getText(R.string.username_too_short)
        username_input_layout.isErrorEnabled = true
    }

    override fun passwordIsShort() {
        password_input_layout.error = resources.getText(R.string.password_too_short)
        password_input_layout.isErrorEnabled = true
    }

    override fun onErrorLogin() {
        Snackbar.make(login_constraint, R.string.error_login, Snackbar.LENGTH_LONG).show()
    }

    override fun validate() {
    }

    override fun validateTwoFactoryAuthorization(phoneMask: String?) {
        val twoFactorDialog = TwoFactorAutentificationDialog(this,
            object : TwoFactorAutentificationDialog.AuthentificatorListener {
                override fun enterCode(code: String) {
                    loginPresenter.enterCode(code)
                }

            })
        twoFactorDialog.show()

        phoneMask?.let { twoFactorDialog.setNumber(it) }

    }

    override fun captcha(captchaUrl: String) {
        val captchaDialog = CaptchaDialog(this, object : CaptchaDialog.CaptchaListener {
            override fun enterCaptcha(captchaKey: String) {
                loginPresenter.enterCaptcha(captchaKey)
            }
        })
        captchaDialog.show()

        captchaDialog.loadCaptcha(captchaUrl)
    }

    override fun initPresenter() = loginPresenter

    override fun getUsername() = username_et.text.toString().trim()

    override fun getPassword() = password_et.text.toString().trim()
}