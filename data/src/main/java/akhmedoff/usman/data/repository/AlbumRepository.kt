package akhmedoff.usman.data.repository

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList


interface AlbumRepository {
    fun saveOwnerId(id: Long)

    fun getOwnerId(): Long

    fun getOwner(): LiveData<Owner>

    fun saveOwner(owner: Owner)

    fun getAlbum(ownerId: String?, albumId: String?): LiveData<ApiResponse<Album>>

    fun getAlbums(ownerId: String? = null): LiveData<PagedList<Album>>

    fun getVideos(
        ownerId: String?,
        videos: String?,
        albumId: String?
    ): LiveData<PagedList<Video>>
}