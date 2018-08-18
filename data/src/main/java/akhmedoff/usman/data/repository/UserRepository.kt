package akhmedoff.usman.data.repository

import akhmedoff.usman.data.BuildConfig
import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.User

class UserRepository(
        private val userSettings: UserSettings,
        private val api: VkApi
) {
    var isLogged = userSettings.isLogged

    fun saveToken(token: String) = userSettings.saveToken(token)

    fun saveUser(user: User) {
        userSettings.saveUserId(user.id)
        userSettings.saveUserName("${user.firstName} ${user.lastName}")
        userSettings.saveUserPhotoUrl(user.photoMaxOrig)
    }

    fun getUserPhotoUrl(): String? {
        return userSettings.getUserPhotoUrl()
    }

    fun getUserName(): String? {
        return userSettings.getUserName()
    }

    fun getCurrentUser() = userSettings.getUserId()

    fun saveCurrentUser(userId: Long) = userSettings.saveUserId(userId)

    fun hasCurrentUser() = userSettings.hasUserId()

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

    fun getUsers(users_id: String? = null) = api.getUsers(listOfNotNull(users_id))

    fun getUsers(ids: List<String>) = api.getUsers(ids)


    fun saveVideoQuality(qualityPosition: Int) = userSettings.saveVideoQuality(qualityPosition)

    fun saveVideoSavePath(path: String) = userSettings.saveVideoSavePath(path)

    fun getVideoQuality(): Int = userSettings.getVideoQuality()

    fun getVideoSavePath(): String = userSettings.getVideoSavePath()
}