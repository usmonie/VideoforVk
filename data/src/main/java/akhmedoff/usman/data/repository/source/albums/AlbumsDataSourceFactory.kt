package akhmedoff.usman.data.repository.source.albums

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.AlbumDao
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.model.Album
import android.arch.paging.DataSource

class AlbumsDataSourceFactory(
    private val vkApi: VkApi,
    private val ownerId: Int?,
    val ownerDao: OwnerDao,
    val albumDao: AlbumDao
) : DataSource.Factory<Int, Album>() {
    override fun create() = AlbumsDataSource(vkApi, ownerId)
}