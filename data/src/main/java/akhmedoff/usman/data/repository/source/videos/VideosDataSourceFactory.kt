package akhmedoff.usman.data.repository.source.videos

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.db.VideoDao
import akhmedoff.usman.data.model.Video
import android.arch.paging.DataSource

class VideosDataSourceFactory(
    private val vkApi: VkApi,
    private val ownerId: Int?,
    private val videoId: String?,
    private val albumId: Int?,
    private val ownerDao: OwnerDao,
    private val videoDao: VideoDao
) : DataSource.Factory<Int, Video> {
    override fun create() =
        VideosDataSource(
            vkApi,
            ownerId,
            videoId,
            albumId,
            ownerDao,
            videoDao
        )
}