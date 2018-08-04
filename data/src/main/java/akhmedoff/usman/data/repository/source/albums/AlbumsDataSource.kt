package akhmedoff.usman.data.repository.source.albums

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.AlbumsResponse
import akhmedoff.usman.data.model.ApiResponse
import android.util.Log
import androidx.paging.PositionalDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumsDataSource(
        private val vkApi: VkApi,
        private val ownerId: String?
) : PositionalDataSource<Album>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Album>) {
        val apiSource = vkApi.getAlbums(
                ownerId = ownerId,
                count = params.requestedLoadSize.toLong(),
                offset = 0
        )

        try {
            val response = apiSource.execute()

            val items = response.body()?.response?.items ?: emptyList()

            callback.onResult(items, 0)
        } catch (exception: Exception) {
            Log.e("exception", exception.toString())
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Album>) {
        vkApi.getAlbums(
                ownerId = ownerId,
                count = params.loadSize.toLong(),
                offset = params.startPosition.toLong()
        ).enqueue(object : Callback<ApiResponse<AlbumsResponse>> {
            override fun onFailure(call: Call<ApiResponse<AlbumsResponse>>?, t: Throwable?) {
                Log.e(javaClass.simpleName, "ERROR: " + t.toString())
            }

            override fun onResponse(
                    call: Call<ApiResponse<AlbumsResponse>>?,
                    response: Response<ApiResponse<AlbumsResponse>>?
            ) {
                response?.body()?.response?.let {
                    callback.onResult(it.items)
                }
            }

        })
    }
}