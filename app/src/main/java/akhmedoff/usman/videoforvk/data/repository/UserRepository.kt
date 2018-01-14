package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.BuildConfig
import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.utils.vkApi

class UserRepository(private val userSettings: UserSettings,
                     private val api: VkApi) {

    var isLoggined = userSettings.isLoggined

    fun saveToken(token: String) {
        userSettings.saveToken(token)
    }

    fun auth(username: String, password: String) =
            vkApi.auth("https://oauth.vk.com/token?", clientId = BuildConfig.VK_APP_ID, clientSecret = BuildConfig.VK_APP_KEY, username = username, password = password, scope = "friends,video,wall,offline,groups,status")

    fun getUser(users_id: String? = null) =
            api.getUser(users_id, token = userSettings.getToken())
}