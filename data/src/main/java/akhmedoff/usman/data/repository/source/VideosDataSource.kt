package akhmedoff.usman.data.repository.source

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.model.ResponseVideo
import akhmedoff.usman.data.model.Video
import android.arch.paging.PositionalDataSource
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosDataSource(
    private val vkApi: VkApi,
    private val ownerId: String?,
    private val videoId: String?,
    private val albumId: String?,
    val ownerDao: OwnerDao
) : PositionalDataSource<Video>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Video>) {
        vkApi.getVideos(
            ownerId = ownerId,
            videos = videoId,
            albumId = albumId,
            count = params.loadSize,
            offset = params.startPosition.toLong(),
            extended = false
        ).enqueue(object : Callback<ResponseVideo> {

            override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                Log.e(javaClass.simpleName, "ERROR: " + t.toString())
            }

            override fun onResponse(
                call: Call<ResponseVideo>?,
                response: Response<ResponseVideo>?
            ) {
                response?.body()?.let {

                    it.profiles?.forEach {
                        ownerDao.insert(it)
                    }

                    it.groups?.forEach {
                        ownerDao.insert(it)
                    }

                    callback.onResult(it.items)
                }
            }

        })
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Video>) {
        val apiSource = vkApi.getVideos(
            ownerId = ownerId,
            videos = videoId,
            albumId = albumId,
            count = params.requestedLoadSize,
            offset = 0,
            extended = false
        )

        try {
            val response = apiSource.execute()

            val items = response.body()?.items ?: emptyList()

            response.body()?.profiles?.forEach {
                ownerDao.insert(it)
            }

            response.body()?.groups?.forEach {
                ownerDao.insert(it)
            }

            callback.onResult(items, 0)
        } catch (exception: Exception) {
            Log.e(javaClass.simpleName, exception.toString())
        }
    }

}