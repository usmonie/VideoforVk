package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Observer

abstract class OwnerPresenter(
    private val videoRepository: VideoRepository
) : BasePresenter<OwnerContract.View>(), OwnerContract.Presenter {

    abstract fun onCreate()

    protected fun loadVideos(id: String) {
        view?.let { view ->
            videoRepository.getVideos(id)
                .observe(view, Observer { videoList -> videoList?.let { view.showVideos(it) } })
        }
    }

    protected fun loadAlbums(id: String) {
        view?.let { view ->
            videoRepository.getAlbums(id)
                .observe(view, Observer { videoList -> videoList?.let { view.showAlbums(it) } })
        }
    }

    override fun clicked(item: Album) {
        view?.showAlbum(item)
    }

    override fun clicked(item: Video) {
        view?.showVideo(item)
    }

    override fun openFriends() {

    }

    override fun openFavourites() {

    }
}