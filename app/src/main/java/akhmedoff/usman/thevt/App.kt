package akhmedoff.usman.thevt

import akhmedoff.usman.data.utils.interceptor
import akhmedoff.usman.data.utils.interceptors.AuthenticationInterceptor
import android.app.Application
import android.support.v7.app.AppCompatDelegate

class App : Application() {
    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        }
    }

    override fun onCreate() {
        super.onCreate()

        interceptor = AuthenticationInterceptor(applicationContext)
    }
}