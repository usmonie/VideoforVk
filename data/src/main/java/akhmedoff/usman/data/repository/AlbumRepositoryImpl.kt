package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.AlbumDao
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.db.VideoDao
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.source.albums.AlbumsDataSourceFactory
import akhmedoff.usman.data.repository.source.videos.VideosDataSourceFactory
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class AlbumRepositoryImpl(
        private val vkApi: VkApi,
        private val userSettings: UserSettings,
        private val ownerDao: OwnerDao,
        private val albumDao: AlbumDao,
        private val videoDao: VideoDao
) : AlbumRepository {

    override fun saveOwnerId(id: Long) = userSettings.saveOwnerId(id)

    override fun getOwnerId() = userSettings.getOwnerId()

    override fun getOwner() = ownerDao.load(getOwnerId())

    override fun saveOwner(owner: Owner) = ownerDao.insert(owner)

    override fun getAlbum(ownerId: Int?, albumId: Int?) =
            vkApi.getAlbum(ownerId?.toString(), albumId?.toString())

    override fun getAlbums(ownerId: String?): LiveData<PagedList<Album>> {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .setPrefetchDistance(8)
                .setInitialLoadSizeHint(50)
                .build()

        val sourceFactory = AlbumsDataSourceFactory(
                vkApi,
                ownerId,
                ownerDao,
                albumDao
        )

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }

    override fun getVideos(
            ownerId: Int?,
            videos: String?,
            albumId: Int?
    ): LiveData<PagedList<Video>> {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .setPrefetchDistance(8)
                .setInitialLoadSizeHint(16)
                .build()

        val sourceFactory = VideosDataSourceFactory(
                vkApi,
                ownerId,
                videos,
                albumId,
                ownerDao,
                videoDao
        )

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }

    override fun getAlbumsByVideo(
            targetId: String?,
            ownerId: String,
            videoId: String
    ) = vkApi.getAlbumsByVideo(targetId, ownerId, videoId)
}