package akhmedoff.usman.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Environment
import android.os.Environment.DIRECTORY_MOVIES

object UserSettings {
    private const val USER_TOKEN = "user_token"
    private const val USER_ID = "user_id"
    private const val USER_NAME = "user_name"
    private const val USER_PHOTO_URL = "user_photo_url"
    private const val OWNER_ID = "owner_id"

    private const val VIDEO_QUALITY = "video_quality"
    private const val VIDEO_SAVE_PATH = "video_save_path"

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

    fun getOwnerId() = sharedPreferences.getLong(OWNER_ID, 0)

    fun saveUserId(id: Long) = sharedPreferences.edit().putString(USER_ID, id.toString()).apply()

    fun getUserId(): String? = sharedPreferences.getString(USER_ID, null)

    fun saveToken(token: String) = sharedPreferences.edit().putString(USER_TOKEN, token).apply()

    fun getToken(): String = sharedPreferences.getString(USER_TOKEN, "")

    fun clear() = sharedPreferences.edit().clear().commit()

    fun hasUserId() = sharedPreferences.contains(USER_ID)

    fun saveUserName(name: String) = sharedPreferences.edit().putString(USER_NAME, name).apply()

    fun saveUserPhotoUrl(photoUrl: String) =
            sharedPreferences.edit().putString(USER_PHOTO_URL, photoUrl).apply()

    fun getUserName(): String = sharedPreferences.getString(USER_NAME, null)

    fun getUserPhotoUrl(): String = sharedPreferences.getString(USER_PHOTO_URL, null)

    fun saveVideoQuality(qualityPosition: Int) = sharedPreferences.edit().putInt(VIDEO_QUALITY, qualityPosition).apply()

    fun saveVideoSavePath(path: String) = sharedPreferences.edit().putString(VIDEO_SAVE_PATH, path).apply()

    fun getVideoQuality(): Int = sharedPreferences.getInt(VIDEO_QUALITY, 0)

    fun getVideoSavePath(): String = sharedPreferences.getString(VIDEO_SAVE_PATH, Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES).absolutePath)
}