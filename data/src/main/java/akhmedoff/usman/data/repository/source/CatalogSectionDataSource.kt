package akhmedoff.usman.data.repository.source

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.ResponseCatalog
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CatalogSectionDataSource(
    private val vkApi: VkApi,
    private val catalogSection: String,
    private val ownerDao: OwnerDao
) : PageKeyedDataSource<String, CatalogItem>() {
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, CatalogItem>
    ) {
        val apiSource = vkApi.getCatalog(
            filters = catalogSection,
            itemsCount = 16,
            count = 1
        )

        try {
            val response = apiSource.execute()

            val items = response.body()?.catalogs?.get(0)?.items ?: emptyList<CatalogItem>()

            callback.onResult(items, null, response.body()?.next)
        } catch (exception: Exception) {
            Log.e(javaClass.simpleName, exception.toString())
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, CatalogItem>
    ) {
        vkApi.getCatalogSection(
            from = params.key,
            section_id = catalogSection,
            count = params.requestedLoadSize
        ).enqueue(object : Callback<ResponseCatalog> {

            override fun onFailure(call: Call<ResponseCatalog>?, t: Throwable?) {
                Log.e(javaClass.simpleName, "ERROR: " + t.toString())
            }

            override fun onResponse(
                call: Call<ResponseCatalog>?,
                response: Response<ResponseCatalog>?
            ) {
                response?.body()?.let {

                    it.profiles?.forEach {
                        ownerDao.insert(it)
                    }

                    it.groups?.forEach {
                        ownerDao.insert(it)
                    }

                    val items =
                        if (it.catalogs.isNotEmpty() && it.catalogs[0].items != null) it.catalogs[0].items!! else emptyList<CatalogItem>()

                    callback.onResult(items, it.next)
                }
            }
        })
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, CatalogItem>
    ) {

    }
}