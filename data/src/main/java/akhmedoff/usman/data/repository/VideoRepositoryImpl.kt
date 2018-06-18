package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.db.VideoDao
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.ResponseVideo
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.source.videos.SearchDataSourceFactory
import akhmedoff.usman.data.repository.source.videos.VideosDataSourceFactory
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import retrofit2.Call

class VideoRepositoryImpl(
        private val vkApi: VkApi,
        private val userSettings: UserSettings,
        private val videoDao: VideoDao,
        private val ownerDao: OwnerDao
) : VideoRepository {
    override fun saveOwnerId(id: Long) = userSettings.saveOwnerId(id)

    override fun getOwnerId() = userSettings.getOwnerId()

    override fun getOwner() = ownerDao.load(getOwnerId())

    override fun getVideos(
            ownerId: Int?,
            videos: String?,
            albumId: Int?
    ): LiveData<PagedList<Video>> {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .setPrefetchDistance(7)
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

    override fun getVideo(video: String): Call<ResponseVideo> =
            vkApi.getVideos(null, video, null, 1, 0)

    override fun saveOwner(owner: Owner) = ownerDao.insert(owner)

    override fun addToAlbum(
            target_id: String?,
            albumIds: List<Int>,
            ownerId: String,
            videoId: String
    ): Call<ApiResponse<Int>> = vkApi.addVideoToAlbum(
            target_id,
            albumIds = albumIds,
            ownerId = ownerId,
            videoId = videoId
    )

    override fun addToUser(
            target_id: String?,
            ownerId: String,
            videoId: String
    ): Call<ApiResponse<Int>> = vkApi.addVideoToAlbum(
            target_id,
            albumId = (-2).toString(),
            ownerId = ownerId,
            videoId = videoId
    )

    override fun search(
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
                .setInitialLoadSizeHint(16)
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
                shorter,
                ownerDao,
                videoDao
        )

        return LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }


    /* fun followOwner(id: Long): Call<ApiResponse<Boolean>> =
         return when {
             id < 0 -> vkApi.joinGroup(id)
             else -> vkApi.
         }*/

    override fun likeVideo(
            ownerId: String?,
            itemId: String,
            captchaSid: String?,
            captchaCode: String?
    ) = vkApi.like("video", ownerId, itemId, captchaSid, captchaCode)

    override fun unlikeVideo(
            ownerId: String?,
            itemId: String,
            captchaSid: String?,
            captchaCode: String?
    ) = vkApi.unlike("video", ownerId, itemId, captchaSid, captchaCode)

    override fun isLiked(userId: String?,
                         type: String,
                         ownerId: String?,
                         itemId: String) =
            vkApi.isLiked(userId, type, ownerId, itemId)

    override fun deleteVideo(ownerId: String, videoId: String) = vkApi.deleteVideo(ownerId = ownerId, videoId = videoId)
}