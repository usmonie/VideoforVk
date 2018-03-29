package akhmedoff.usman.data.repository.source.videos

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.db.VideoDao
import akhmedoff.usman.data.model.Video
import android.arch.paging.DataSource

class SearchDataSourceFactory(
    private val vkApi: VkApi,
    private val query: String,
    private val sort: Int?,
    private val hd: Int?,
    private val adult: Int?,
    private val filters: String?,
    private val searchOwn: Boolean?,
    private val longer: Long?,
    private val shorter: Long?,
    private val ownerDao: OwnerDao,
    private val videoDao: VideoDao
) : DataSource.Factory<Int, Video>() {
    override fun create() =
        SearchDataSource(
            vkApi,
            query,
            sort,
            hd,
            adult,
            filters,
            searchOwn,
            longer,
            shorter,
            ownerDao,
            videoDao
        )
}