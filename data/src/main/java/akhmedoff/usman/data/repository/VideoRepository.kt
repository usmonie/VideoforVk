package akhmedoff.usman.data.repository

import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.ResponseVideo
import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import retrofit2.Call

interface VideoRepository {

    fun saveOwnerId(id: Long)

    fun getOwnerId(): Long

    fun getOwner(): LiveData<Owner>

    fun getVideos(
        ownerId: String? = null,
        videos: String? = null,
        albumId: String? = null
    ): LiveData<PagedList<Video>>

    fun getVideo(video: String): Call<ResponseVideo>

    fun saveOwner(owner: Owner)

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
}