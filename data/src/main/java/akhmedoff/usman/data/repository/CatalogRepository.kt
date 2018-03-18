package akhmedoff.usman.data.repository

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList


interface CatalogRepository {
    fun getCatalog(filters: String): LiveData<PagedList<Catalog>>
    fun getCatalogSection(catalogId: String): LiveData<PagedList<CatalogItem>>
}