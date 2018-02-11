package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.Video
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

class VideoRepository(private val vkApi: VkApi) {

    fun getVideos(
        ownerId: String? = null,
        videos: String? = null,
        albumId: String? = null
    ): LiveData<PagedList<Video>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(15)
            .setInitialLoadSizeHint(10)
            .build()

        val sourceFactory = VideosDataSourceFactory(vkApi, ownerId, videos, albumId)

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }

    fun getCatalog(): LiveData<PagedList<Catalog>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(15)
            .setPrefetchDistance(15)
            .setInitialLoadSizeHint(20)
            .build()

        return LivePagedListBuilder(CatalogsDataSourceFactory(vkApi), pagedListConfig).build()
    }

    fun getVideo(video: String) = vkApi.getVideos(null, video, null, 1, 0)

    fun getAlbum(ownerId: String?, albumId: String?) = vkApi.getAlbum(ownerId, albumId)
}