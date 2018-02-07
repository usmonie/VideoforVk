package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.ResponseCatalog
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import akhmedoff.usman.videoforvk.model.Response as ApiResponse

class CatalogsPageKeyedDataSource(
    private val vkApi: VkApi,
    private val token: String
) : PageKeyedDataSource<String, Catalog>() {

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Catalog>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Catalog>) {
        vkApi.getCatalog(
            count = 4,
            itemsCount = 7,
            from = params.key,
            token = token
        ).enqueue(object : Callback<ResponseCatalog> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<ResponseCatalog>?, t: Throwable?) {
                Log.d("callback", "ERROR: " + t.toString())
            }

            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(
                call: Call<ResponseCatalog>?,
                response: Response<ResponseCatalog>?
            ) {
                response?.body()?.let {
                    callback.onResult(it.catalogs, it.next)
                }
            }

        })
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Catalog>
    ) {
        val apiSource = vkApi.getCatalog(
            count = 4,
            itemsCount = 7,
            filters = "top,feed,ugc,series,other",
            token = token
        )

        try {
            val response = apiSource.execute()

            val items = response.body()?.catalogs ?: emptyList<Catalog>()

            callback.onResult(items, null, response.body()?.next)
        } catch (exception: Exception) {
            Log.d("exception", exception.toString())
        }

    }
}