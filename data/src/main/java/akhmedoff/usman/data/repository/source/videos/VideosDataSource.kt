package akhmedoff.usman.data.repository.source.videos

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.db.VideoDao
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
    val ownerDao: OwnerDao,
    val videoDao: VideoDao
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
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                Log.e("callback", "ERROR: " + t.toString())
            }

            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(
                call: Call<ResponseVideo>?,
                response: Response<ResponseVideo>?
            ) {
                response?.body()?.let {
                    videoDao.insert(it.items)

                    it.profiles?.forEach {
                        ownerDao.insert(it)
                    }

                    it.groups?.forEach {
                        ownerDao.insert(it)
                    }

                }
            }

        })
        callback.onResult(videoDao.loadAll(params.loadSize, params.startPosition))

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

        var items: List<Video> = mutableListOf()
        try {
            val response = apiSource.execute()

            items = response.body()?.items ?: emptyList()

            response.body()?.profiles?.forEach {
                ownerDao.insert(it)
            }

            response.body()?.groups?.forEach {
                ownerDao.insert(it)
            }

        } catch (exception: Exception) {
            Log.e("exception", exception.toString())
        } finally {
            videoDao.insert(items)
        }

        callback.onResult(videoDao.loadAll(params.pageSize, 0), 0)
    }

}