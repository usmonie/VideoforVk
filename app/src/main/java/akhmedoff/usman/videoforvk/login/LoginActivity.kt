package akhmedoff.usman.videoforvk.login

import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.utils.vkApi
import akhmedoff.usman.videoforvk.CaptchaDialog
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.main.MainActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {
    override lateinit var loginPresenter: LoginContract.Presenter

    private lateinit var twoFactorDialog: TwoFactorAuthenticationDialog

    private val captchaDialog: CaptchaDialog by lazy {
        CaptchaDialog(this, {
            loginPresenter.enterCaptcha(it)

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loginPresenter = LoginPresenter(
            UserRepository(
                UserSettings.getUserSettings(applicationContext),
                vkApi
            )
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        twoFactorDialog = TwoFactorAuthenticationDialog(this, {
            loginPresenter.enterCode(it)
        }) {
            twoFactorDialog.hide()
        }

        login_button.setOnClickListener { loginPresenter.login() }

        password_et.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                loginPresenter.login()
                handled = true
            }
            handled
        }
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

    override fun showCodeError() =
        twoFactorDialog.showErrorCode(getString(R.string.error_code))


    override fun onErrorLogin() =
        Snackbar.make(login_constraint, R.string.error_login, Snackbar.LENGTH_LONG).show()


    override fun validateTwoFactoryAuthorization(phoneMask: String?) {
        twoFactorDialog.show()
        phoneMask?.let { twoFactorDialog.setNumber(it) }
    }

    override fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun showProgress() {
        login_progress.visibility = View.VISIBLE
        login_progress.animate()
    }

    override fun hideProgress() {
        login_progress.visibility = View.GONE
    }

    override fun setButtonEnabled(enabled: Boolean) {
        login_button.isEnabled = enabled
    }

    override fun editTextEditable(editable: Boolean) {
        password_et.isEnabled = editable
        username_et.isEnabled = editable
    }

    override fun isDialogShows(): Boolean = twoFactorDialog.isShowing || captchaDialog.isShowing

    override fun showDialogLoading() {
        when {
            twoFactorDialog.isShowing -> twoFactorDialog.showLoading()
        }
    }

    override fun hideDialogLoading() = twoFactorDialog.hideLoading()

    override fun captcha(captchaUrl: String) {
        captchaDialog.show()
        captchaDialog.loadCaptcha(captchaUrl)
    }

    override fun initPresenter() = loginPresenter

    override fun getUsername() = username_et.text.toString().trim()

    override fun getPassword() = password_et.text.toString().trim()
}