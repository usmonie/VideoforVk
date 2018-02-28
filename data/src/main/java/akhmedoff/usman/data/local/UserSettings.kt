package akhmedoff.usman.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object UserSettings {
    private const val USER_TOKEN = "user_token"
    private const val OWNER_ID = "owner_id"

    fun getUserSettings(context: Context): UserSettings {
        val userSettings = UserSettings
        userSettings.sharedPreferences =
                context.getSharedPreferences("user_settings", MODE_PRIVATE)
        return userSettings
    }

    private lateinit var sharedPreferences: SharedPreferences

    var isLogged = false
        get() = sharedPreferences.contains(USER_TOKEN)


    fun saveOwnerId(id: Long) = sharedPreferences.edit().putLong(OWNER_ID, id).apply()

    fun saveToken(token: String) = sharedPreferences.edit().putString(USER_TOKEN, token).apply()

    fun getToken(): String = sharedPreferences.getString(USER_TOKEN, "")

    fun clear() = sharedPreferences.edit().clear().apply()

    fun getOwnerId() = sharedPreferences.getLong(OWNER_ID, 0)
}