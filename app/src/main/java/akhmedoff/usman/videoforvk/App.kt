package akhmedoff.usman.videoforvk

import akhmedoff.usman.data.utils.interceptor
import akhmedoff.usman.data.utils.interceptors.AuthenticationInterceptor
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        init {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        interceptor = AuthenticationInterceptor(context)
    }
}