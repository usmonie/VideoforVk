package akhmedoff.usman.videoforvk.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object UserSettings {
    private const val USER_NAME = "user_name"
    private const val USER_TOKEN = "user_token"
    private const val USER_PHOTO = "user_photo"

    fun getUserSettings(context: Context): UserSettings {
        val userSettings = UserSettings
        userSettings.sharedPreferences =
                context.getSharedPreferences("user_settings", MODE_PRIVATE)
        return userSettings
    }

    private lateinit var sharedPreferences: SharedPreferences

    var isLoggined = false
        get() = sharedPreferences.contains(USER_TOKEN)

    fun saveToken(token: String) = sharedPreferences.edit().putString(USER_TOKEN, token).apply()

    fun getToken(): String = sharedPreferences.getString(USER_TOKEN, "")
}