package akhmedoff.usman.videoforvk.albums

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer

class AlbumsPresenter(
    override var view: AlbumsContract.View?,
    private val videosRepository: VideoRepository
) : AlbumsContract.Presenter {

    override fun onCreated() = refresh()

    override fun onDestroyed() {
        view = null
    }

    override fun refresh() {
        view?.let { view ->
            videosRepository
                .getAlbums()
                .observe(view, Observer {
                    if (it != null) {
                        view.setList(it)
                    } else {
                        view.showEmptyList()
                    }
                })
        }
    }

    override fun onItemClicked(item: Album) {
    }
}