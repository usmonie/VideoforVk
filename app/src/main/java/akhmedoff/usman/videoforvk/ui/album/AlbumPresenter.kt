package akhmedoff.usman.videoforvk.ui.album

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.AlbumRepository
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList

class AlbumPresenter(private var view: AlbumContract.View?, private val albumRepository: AlbumRepository) :
    AlbumContract.Presenter {

    override fun error(error: Error, message: String) {

    }

    override fun onCreated() {
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
    }

    override fun clickAdd() {
    }
}