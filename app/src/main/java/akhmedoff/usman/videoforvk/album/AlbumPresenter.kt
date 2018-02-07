package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Video
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.paging.PagedList

class AlbumPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<AlbumContract.View>(), AlbumContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        view?.let {
            videoRepository.getVideos(
                ownerId = it.getAlbumOwnerId(),
                videos = null,
                albumId = it.getAlbumId()
            )
                .observe(it, Observer { pagedList: PagedList<Video>? ->
                    pagedList?.let { items -> it.showVideos(items) }
                })
        }
    }

    override fun clickVideo(video: Video) {
        view?.showVideo(video)
    }

    override fun clickAdd() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}