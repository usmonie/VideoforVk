package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.data.local.UserSettings

class VideoRepository(private val userSettings: UserSettings, private val vkApi: VkApi) {

    fun getVideos(
        ownerId: Int? = null,
        videos: String? = null,
        albumId: String? = null,
        count: Int = 15,
        offset: Long = 0
    ) = vkApi.getVideos(ownerId, videos, albumId, count, offset, userSettings.getToken())

    fun getCatalog(items: Int, from: String? = null, filters: String = "other") =
        vkApi.getCatalog(items, from = from, filters = filters, token = userSettings.getToken())

    fun getVideo(video: String) =
        vkApi.getVideos(null, video, null, 1, 0, token = userSettings.getToken())
}