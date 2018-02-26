package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.source.AlbumsDataSourceFactory
import akhmedoff.usman.data.repository.source.CatalogsDataSourceFactory
import akhmedoff.usman.data.repository.source.SearchDataSourceFactory
import akhmedoff.usman.data.repository.source.VideosDataSourceFactory
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

        val sourceFactory = VideosDataSourceFactory(
            vkApi,
            ownerId,
            videos,
            albumId
        )

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }

    fun getCatalog(): LiveData<PagedList<Catalog>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(15)
            .setPrefetchDistance(15)
            .setInitialLoadSizeHint(20)
            .build()

        return LivePagedListBuilder(
            CatalogsDataSourceFactory(
                vkApi
            ), pagedListConfig
        ).build()
    }

    fun getVideo(video: String) = vkApi.getVideos(null, video, null, 1, 0)

    fun getAlbum(ownerId: String?, albumId: String?) = vkApi.getAlbum(ownerId, albumId)

    fun getAlbums(ownerId: String): LiveData<PagedList<Album>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(15)
            .setInitialLoadSizeHint(10)
            .build()

        val sourceFactory = AlbumsDataSourceFactory(
            vkApi,
            ownerId
        )

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }

    fun search(
        query: String,
        sort: Int?,
        hd: Int?,
        adult: Int?,
        filters: String?,
        searchOwn: Boolean?,
        longer: Long?,
        shorter: Long?
    ): LiveData<PagedList<Video>> {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(15)
            .setInitialLoadSizeHint(10)
            .build()

        val sourceFactory = SearchDataSourceFactory(
            vkApi,
            query,
            sort,
            hd,
            adult,
            filters,
            searchOwn,
            longer,
            shorter
        )

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }
}