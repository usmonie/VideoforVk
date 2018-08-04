package akhmedoff.usman.data.repository.source.videos

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.VideoDao
import akhmedoff.usman.data.model.Video
import androidx.paging.DataSource

class FaveDataSourceFactory(
        private val vkApi: VkApi,
        private val videoDao: VideoDao
) : DataSource.Factory<Int, Video>() {
    override fun create() = FaveDataSource(vkApi, videoDao)
}