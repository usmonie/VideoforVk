package akhmedoff.usman.videoforvk

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Point
import android.view.WindowManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val manager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        display.getSize(SCREEN_SIZE)
    }

    companion object {

        val SCREEN_SIZE = Point()

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}