package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.repository.source.AlbumsDataSourceFactory
import akhmedoff.usman.data.repository.source.CatalogsDataSourceFactory
import akhmedoff.usman.data.repository.source.SearchDataSourceFactory
import akhmedoff.usman.data.repository.source.VideosDataSourceFactory
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import retrofit2.Call

class VideoRepository(
    private val vkApi: VkApi,
    private val userSettings: UserSettings,
    private val ownerDao: OwnerDao
) {
    fun saveOwnerId(id: Long) = userSettings.saveOwnerId(id)

    fun getOwnerId() = userSettings.getOwnerId()

    fun getOwner() = ownerDao.load(getOwnerId())

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
            albumId, ownerDao
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

    fun getVideo(video: String): Call<ResponseVideo> = vkApi.getVideos(null, video, null, 1, 0)

    fun saveOwner(owner: Owner) = ownerDao.insert(owner)

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