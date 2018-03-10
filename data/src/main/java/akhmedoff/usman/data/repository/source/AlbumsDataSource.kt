package akhmedoff.usman.data.repository.source

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.ApiResponse
import android.arch.paging.PositionalDataSource
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumsDataSource(
    private val vkApi: VkApi,
    private val ownerId: String
) : PositionalDataSource<Album>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Album>) {
        val apiSource = vkApi.getAlbums(
            ownerId = ownerId,
            count = 15,
            offset = 0
        )

        try {
            val response = apiSource.execute()

            val items = response.body()?.response ?: emptyList()

            callback.onResult(items, 0)
        } catch (exception: Exception) {
            Log.e("exception", exception.toString())
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Album>) {
        vkApi.getAlbums(
            ownerId = ownerId,
            count = 15,
            offset = 0
        ).enqueue(object : Callback<ApiResponse<List<Album>>> {
            override fun onFailure(call: Call<ApiResponse<List<Album>>>?, t: Throwable?) {
                Log.e(javaClass.simpleName, "ERROR: " + t.toString())
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Album>>>?,
                response: Response<ApiResponse<List<Album>>>?
            ) {
                response?.body()?.response?.let {
                    callback.onResult(it)
                }
            }

        })
    }
}