package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.paging.PagedList

class AlbumPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<AlbumContract.View>(), AlbumContract.Presenter {

    override fun error(error: Error, message: String) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        loadAlbum()
        loadAlbumVideos()
    }

    private fun loadAlbumVideos() {
        view?.let { view ->
            videoRepository.getVideos(
                ownerId = view.getAlbumOwnerId(),
                videos = null,
                albumId = view.getAlbumId()
            ).observe(view, Observer { pagedList: PagedList<Video>? ->
                pagedList?.let { items -> view.showVideos(items) }
            })
        }
    }

    private fun loadAlbum() {
        view?.let { view ->
            videoRepository.getAlbum(
                ownerId = view.getAlbumOwnerId(),
                albumId = view.getAlbumId()
            ).observe(view, Observer { albumResponse ->
                run {
                    albumResponse?.response?.let { album ->
                        run {
                            view.showAlbumImage(album.photo320)
                            when (view.getAlbumTitle()) {
                                null -> view.showAlbumTitle(album.title)
                                else -> view.showAlbumTitle(view.getAlbumTitle()!!)
                            }
                        }
                    }
                }
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