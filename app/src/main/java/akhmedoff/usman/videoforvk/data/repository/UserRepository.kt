package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.BuildConfig
import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.data.local.UserSettings

class UserRepository(
    private val userSettings: UserSettings,
    private val api: VkApi
) {

    var isLogged = userSettings.isLogged

    fun saveToken(token: String) {
        userSettings.saveToken(token)
    }

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

    fun getUser(users_id: String? = null) = api.getUser(users_id)

}