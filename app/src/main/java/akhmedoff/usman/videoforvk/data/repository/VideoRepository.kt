package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.model.Catalog
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

class VideoRepository(
    private val userSettings: UserSettings,
    private val vkApi: VkApi
) {

    fun getVideos(
        ownerId: Int? = null,
        videos: String? = null,
        albumId: String? = null,
        count: Int = 15,
        offset: Long = 0
    ) = vkApi.getVideos(ownerId, videos, albumId, count, offset, userSettings.getToken())

    fun getCatalog(): LiveData<PagedList<Catalog>> {
        val sourceFactory = CatalogDataSourceFactory(vkApi, userSettings.getToken())

        return LivePagedListBuilder(sourceFactory, 10).build()
    }

    fun getVideo(video: String) =
        vkApi.getVideos(null, video, null, 1, 0, token = userSettings.getToken())
}