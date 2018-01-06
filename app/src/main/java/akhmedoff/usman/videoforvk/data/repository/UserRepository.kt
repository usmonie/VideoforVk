package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.data.local.UserSettings

class UserRepository(private val userSettings: UserSettings,
                     private val api: VkApi) {

    var isLoggined = userSettings.isLoggined

    fun saveToken(token: String) {
        userSettings.saveToken(token)
    }

    fun getUser(users_id: String? = null) =
            api.getUser(users_id, token = userSettings.getToken())
}