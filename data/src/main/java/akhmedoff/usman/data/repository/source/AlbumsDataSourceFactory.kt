package akhmedoff.usman.data.repository.source

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.Album
import android.arch.paging.DataSource

class AlbumsDataSourceFactory(
    private val vkApi: VkApi,
    private val ownerId: String
) : DataSource.Factory<Int, Album> {
    override fun create() = AlbumsDataSource(vkApi, ownerId)
}