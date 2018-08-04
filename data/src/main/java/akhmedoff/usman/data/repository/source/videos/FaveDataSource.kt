package akhmedoff.usman.data.repository.source.videos

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.VideoDao
import akhmedoff.usman.data.model.ResponseVideo
import akhmedoff.usman.data.model.Video
import android.util.Log
import androidx.paging.PositionalDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaveDataSource(private val vkApi: VkApi,
                     val videoDao: VideoDao
) : PositionalDataSource<Video>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Video>) {
        vkApi
                .getFaveVideos(params.startPosition, params.loadSize)
                .enqueue(object : Callback<ResponseVideo> {

                    override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                        Log.e(javaClass.simpleName, "ERROR: " + t.toString())
                        //callback.onResult(videoDao.loadFaveVideos(params.startPosition, params.loadSize))

                    }

                    override fun onResponse(
                            call: Call<ResponseVideo>?,
                            response: Response<ResponseVideo>?
                    ) {
                        response?.body()?.let { responseVideo ->
                            Log.d(javaClass.simpleName, "RESPONSE:" + response.message())

                            callback.onResult(responseVideo.items)
                        }
                    }

                })

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Video>) {
        val apiSource = vkApi.getFaveVideos(params.requestedStartPosition, params.pageSize)

        try {
            val response = apiSource.execute()

            val items = response.body()?.items ?: emptyList()
            callback.onResult(items, 0)
        } catch (exception: Exception) {
            Log.e(javaClass.simpleName, exception.toString())
            //callback.onResult(videoDao.loadFaveVideos(params.requestedStartPosition, params.requestedLoadSize), 0)
        }
    }
}