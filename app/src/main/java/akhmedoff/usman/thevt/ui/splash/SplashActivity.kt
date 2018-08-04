package akhmedoff.usman.thevt.ui.splash

import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.utils.vkApi
import akhmedoff.usman.thevt.ui.login.LoginActivity
import akhmedoff.usman.thevt.ui.main.MainActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity(), SplashContract.View {

    override lateinit var splashPresenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashPresenter = SplashPresenter(
                this,
                UserRepository(
                        UserSettings.getUserSettings(applicationContext),
                        vkApi
                )
        )

        splashPresenter.onCreate()
    }

    override fun showMain() = startActivity(Intent(this, MainActivity::class.java))

    override fun showLogin() = startActivity(Intent(this, LoginActivity::class.java))

    override fun onStop() {
        super.onStop()
        splashPresenter.onDestroy()
    }
}