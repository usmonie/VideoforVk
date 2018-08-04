package akhmedoff.usman.data.repository

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import androidx.lifecycle.LiveData
import androidx.paging.PagedList


interface CatalogRepository {

    fun getCatalog(filters: String): LiveData<PagedList<Catalog>>

    fun getCatalogSection(catalogId: String): LiveData<PagedList<CatalogItem>>
}