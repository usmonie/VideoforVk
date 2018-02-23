package akhmedoff.usman.data.repository

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.Catalog
import android.arch.paging.DataSource

class CatalogsDataSourceFactory(private val vkApi: VkApi) : DataSource.Factory<String, Catalog> {
    /**
     * Create a DataSource.
     *
     *
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     *
     *
     * [LivePagedListBuilder] for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * `LiveData<PagedList>` to observers.
     *
     * @return the new DataSource.
     */
    override fun create() = CatalogsPageKeyedDataSource(vkApi)
}