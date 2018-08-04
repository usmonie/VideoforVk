package akhmedoff.usman.data.repository.source.catalogs

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.CatalogDao
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.model.CatalogItem
import androidx.paging.DataSource

class CatalogSectionDataSourceFactory(
        private val vkApi: VkApi,
        private val catalogSection: String,
        private val ownerDao: OwnerDao,
        private val catalogDao: CatalogDao
) : DataSource.Factory<String, CatalogItem>() {
    override fun create() = CatalogSectionDataSource(
            vkApi,
            catalogSection,
            ownerDao,
            catalogDao
    )
}