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
    private val ownerId: Int?,
    private val videoId: String?,
    private val albumId: Int?,
    val ownerDao: OwnerDao,
    val videoDao: VideoDao
) : PositionalDataSource<Video>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Video>) {

        vkApi.getVideos(
            ownerId = ownerId?.toString(),
            videos = videoId,
            albumId = albumId?.toString(),
            count = params.loadSize,
            offset = params.startPosition.toLong(),
            extended = false
        ).enqueue(object : Callback<ResponseVideo> {
            override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                Log.e("callback", "ERROR: " + t.toString())
                callback.onResult(videoDao.loadAll(params.loadSize, params.startPosition, ownerId))

            }

            override fun onResponse(
                call: Call<ResponseVideo>?,
                response: Response<ResponseVideo>?
            ) {
                response?.body()?.let { responseVideo ->
                    videoDao.insert(responseVideo.items.mapIndexed { index, video ->
                        ownerId?.toString()?.let { ownerId ->
                            video.userIds.add(ownerId)
                        }
                        video.roomId = params.startPosition + index
                        video
                    })

                    responseVideo.profiles?.forEach {
                        ownerDao.insert(it)
                    }

                    responseVideo.groups?.forEach {
                        ownerDao.insert(it)
                    }
                    callback.onResult(
                        videoDao.loadAll(
                            params.loadSize,
                            params.startPosition,
                            ownerId
                        )
                    )

                }
            }

        })


    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Video>) {
        val apiSource = vkApi.getVideos(
            ownerId = ownerId?.toString(),
            videos = videoId,
            albumId = albumId?.toString(),
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
            ownerId?.let { ownerId ->
                videoDao.clear(ownerId)
                items.forEachIndexed { index, video ->
                    video.userIds.add(ownerId.toString())
                    video.roomId = index
                }
            }

            videoDao.insert(items)

        } catch (exception: Exception) {
            Log.e("exception", exception.toString())
        }

        callback.onResult(videoDao.loadAll(params.requestedLoadSize, 0, ownerId), 0)
    }

}