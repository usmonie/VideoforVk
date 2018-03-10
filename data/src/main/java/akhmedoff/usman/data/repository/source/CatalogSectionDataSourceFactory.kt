package akhmedoff.usman.data.repository.source

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.model.CatalogItem
import android.arch.paging.DataSource

class CatalogSectionDataSourceFactory(
    private val vkApi: VkApi,
    private val catalogSection: String,
    private val ownerDao: OwnerDao
) : DataSource.Factory<String, CatalogItem> {
    override fun create() =
        CatalogSectionDataSource(vkApi, catalogSection, ownerDao)
}