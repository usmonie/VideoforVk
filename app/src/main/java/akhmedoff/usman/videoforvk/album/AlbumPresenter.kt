package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.AlbumRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.paging.PagedList

class AlbumPresenter(private val albumRepository: AlbumRepository) :
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
            albumRepository.getVideos(
                ownerId = view.getAlbumOwnerId()?.toInt(),
                videos = null,
                albumId = view.getAlbumId()?.toInt()
            ).observe(view, Observer { pagedList: PagedList<Video>? ->
                pagedList?.let { items -> view.showVideos(items) }
            })
        }
    }

    private fun loadAlbum() {
        view?.let { view ->
            albumRepository
                .getAlbum(
                    ownerId = view.getAlbumOwnerId()?.toInt(),
                    albumId = view.getAlbumId()?.toInt()
                ).observe(view, Observer { albumResponse ->
                    albumResponse?.response?.let { album ->
                        album.photo320?.let { view.showAlbumImage(it) }
                        when (view.getAlbumTitle()) {
                            null -> view.showAlbumTitle(album.title)
                            else -> view.showAlbumTitle(view.getAlbumTitle()!!)
                        }

                    }
                })
        }
    }

    override fun clickVideo(video: Video) {
        view?.showVideo(video)
    }

    override fun clickAdd() {
    }
}