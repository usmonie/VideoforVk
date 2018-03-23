package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.CatalogDao
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.repository.source.catalogs.CatalogSectionDataSourceFactory
import akhmedoff.usman.data.repository.source.catalogs.CatalogsDataSourceFactory
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

class CatalogRepositoryImpl(
    private val vkApi: VkApi,
    private val ownerDao: OwnerDao,
    private val catalogDao: CatalogDao
) : CatalogRepository {

    override fun getCatalogSection(catalogId: String): LiveData<PagedList<CatalogItem>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(16)
            .setPrefetchDistance(10)
            .setInitialLoadSizeHint(16)
            .build()

        return LivePagedListBuilder(
            CatalogSectionDataSourceFactory(
                vkApi,
                catalogId,
                ownerDao,
                catalogDao
            ),
            pagedListConfig
        ).build()
    }

    override fun getCatalog(filters: String): LiveData<PagedList<Catalog>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(8)
            .setInitialLoadSizeHint(16)
            .build()

        return LivePagedListBuilder(
            CatalogsDataSourceFactory(
                vkApi,
                filters,
                catalogDao
            ), pagedListConfig
        ).build()
    }
}