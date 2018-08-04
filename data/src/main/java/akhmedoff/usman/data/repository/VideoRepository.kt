package akhmedoff.usman.data.repository

import akhmedoff.usman.data.model.*
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import retrofit2.Call

interface VideoRepository {

    fun saveOwnerId(id: Long)

    fun getOwnerId(): Long

    fun getOwner(): LiveData<Owner>

    fun getVideos(
            ownerId: Int? = null,
            videos: String? = null,
            albumId: Int? = null
    ): LiveData<PagedList<Video>>

    fun getFaveVideos(): LiveData<PagedList<Video>>

    fun getVideo(video: String): Call<ResponseVideo>

    fun saveOwner(owner: Owner)

    fun addToAlbum(
            target_id: String? = null,
            albumIds: List<Int>,
            ownerId: String,
            videoId: String
    ): Call<ApiResponse<Int>>

    fun addToUser(
            target_id: String? = null,
            ownerId: String,
            videoId: String
    ): Call<ApiResponse<Int>>

    fun search(
            query: String,
            sort: Int?,
            hd: Int?,
            adult: Int?,
            filters: String?,
            searchOwn: Boolean?,
            longer: Long?,
            shorter: Long?
    ): LiveData<PagedList<Video>>

    fun likeVideo(
            ownerId: String? = null,
            itemId: String,
            captchaSid: String? = null,
            captchaCode: String? = null
    ): Call<ApiResponse<Likes>>

    fun unlikeVideo(
            ownerId: String?,
            itemId: String,
            captchaSid: String?,
            captchaCode: String?
    ): Call<ApiResponse<Likes>>

    fun isLiked(userId: String? = null,
                type: String,
                ownerId: String? = null,
                itemId: String
    ): Call<ApiResponse<Liked>>

    fun deleteVideo(ownerId: String, videoId: String): Call<ApiResponse<Int>>
}