package akhmedoff.usman.videoforvk.splash

import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.login.LoginActivity
import akhmedoff.usman.videoforvk.main.MainActivity
import akhmedoff.usman.videoforvk.utils.vkApi
import android.content.Intent
import android.os.Bundle

class SplashActivity : BaseActivity<SplashContract.View, SplashContract.Presenter>(),
    SplashContract.View {


    override lateinit var splashPresenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        splashPresenter = SplashPresenter(
            UserRepository(
                UserSettings.getUserSettings(applicationContext),
                vkApi
            )
        )
        super.onCreate(savedInstanceState)

    }

    override fun showMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun initPresenter() = splashPresenter
}