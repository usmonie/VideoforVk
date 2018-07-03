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
                    callback.onResult(responseVideo.items)
                    videoDao.insert(responseVideo.items.mapIndexed { index, video ->
                        ownerId?.toString()?.let { ownerId ->
                            video.userIds.add(ownerId)
                        }
                        video.roomId = params.startPosition + index
                        return@mapIndexed video
                    })

                    responseVideo.profiles?.let { ownerDao.insertAll(it) }

                    responseVideo.groups?.let { ownerDao.insertAll(it) }

                }

                if (response?.body() != null) {
                    callback.onResult(response.body()!!.items)
                    videoDao.insert(response.body()!!.items.mapIndexed { index, video ->
                        ownerId?.toString()?.let { ownerId ->
                            video.userIds.add(ownerId)
                        }
                        video.roomId = params.startPosition + index
                        return@mapIndexed video
                    })

                    response.body()?.profiles?.let { ownerDao.insertAll(it) }

                    response.body()?.groups?.let { ownerDao.insertAll(it) }
                } else
                    callback.onResult(
                            videoDao.loadAll(
                                    params.loadSize,
                                    params.startPosition,
                                    ownerId
                            )
                    )
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
            if (response.isSuccessful) {
                callback.onResult(items, 0)
                response.body()?.profiles?.let { ownerDao.insertAll(it) }

                response.body()?.groups?.let { ownerDao.insertAll(it) }

                ownerId?.let { ownerId ->
                    videoDao.clear(ownerId)
                    items.forEachIndexed { index, video ->
                        video.userIds.add(ownerId.toString())
                        video.roomId = index
                    }
                }

                videoDao.insert(items)
            } else {
                callback.onResult(videoDao.loadAll(params.requestedLoadSize, 0, ownerId), 0)
            }

        } catch (exception: Exception) {
            Log.e("exception", exception.toString())
            callback.onResult(videoDao.loadAll(params.requestedLoadSize, 0, ownerId), 0)
        }

    }

}