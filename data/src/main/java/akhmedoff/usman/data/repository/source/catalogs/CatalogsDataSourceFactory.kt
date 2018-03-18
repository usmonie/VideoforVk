package akhmedoff.usman.data.repository.source.catalogs

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.CatalogDao
import akhmedoff.usman.data.model.Catalog
import android.arch.paging.DataSource

class CatalogsDataSourceFactory(
    private val vkApi: VkApi,
    private val filters: String,
    val catalogDao: CatalogDao
) :
    DataSource.Factory<String, Catalog> {

    override fun create() =
        CatalogsPageKeyedDataSource(vkApi, filters)
}