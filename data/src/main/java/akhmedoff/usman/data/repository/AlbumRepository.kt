package akhmedoff.usman.data.repository

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.Video
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import retrofit2.Call


interface AlbumRepository {
    fun saveOwnerId(id: Long)

    fun getOwnerId(): Long

    fun getOwner(): LiveData<Owner>

    fun saveOwner(owner: Owner)

    fun getAlbum(ownerId: Int?, albumId: Int?): Call<ApiResponse<Album>>

    fun getAlbums(ownerId: String? = null): LiveData<PagedList<Album>>

    fun getAlbumsByVideo(
            targetId: String? = null,
            ownerId: String,
            videoId: String
    ): Call<ApiResponse<List<Int>>>

    fun getVideos(
            ownerId: Int?,
            videos: String?,
            albumId: Int?
    ): LiveData<PagedList<Video>>
}