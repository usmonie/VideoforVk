package akhmedoff.usman.data.repository.source

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.Album
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

            val items = response.body() ?: emptyList()

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
        ).enqueue(object : Callback<List<Album>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<Album>>?, t: Throwable?) {
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
                call: Call<List<Album>>?,
                response: Response<List<Album>>?
            ) {
                response?.body()?.let {
                    callback.onResult(it)
                }
            }

        })
    }
}