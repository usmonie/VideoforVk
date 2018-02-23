package akhmedoff.usman.videoforvk.data.repository

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.model.Video
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
    private val shorter: Long?
) : DataSource.Factory<Int, Video> {
    override fun create() =
        SearchDataSource(vkApi, query, sort, hd, adult, filters, searchOwn, longer, shorter)
}