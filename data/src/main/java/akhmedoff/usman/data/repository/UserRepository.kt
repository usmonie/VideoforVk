package akhmedoff.usman.data.repository

import akhmedoff.usman.data.BuildConfig
import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.User
import android.arch.core.util.Function
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

class UserRepository(
    private val userSettings: UserSettings,
    private val api: VkApi
) {
    var isLogged = userSettings.isLogged

    fun saveToken(token: String) = userSettings.saveToken(token)

    fun checkToken() =
        api.checkToken("https://api.vk.com/method/secure.checkToken?token=" + userSettings.getToken())

    fun clear() = userSettings.clear()

    fun auth(
        username: String,
        password: String,
        captchaSid: String? = null,
        captchaKey: String? = null,
        code: String? = null
    ) = api.auth(
        "https://oauth.vk.com/token?",
        BuildConfig.VK_APP_ID,
        BuildConfig.VK_APP_KEY,
        username,
        password,
        "friends,video,wall,offline,groups,status",
        code = code,
        captchaSid = captchaSid,
        captchaKey = captchaKey
    )

    fun getUsers(users_id: String? = null): LiveData<List<User>> =
        Transformations.map(api.getUsers(listOf(users_id)), Function {
            return@Function when {
                it.response != null -> it.response
                else -> listOf()
            }
        })

    fun getUsers(ids: List<String>) = api.getUsers(ids)
}