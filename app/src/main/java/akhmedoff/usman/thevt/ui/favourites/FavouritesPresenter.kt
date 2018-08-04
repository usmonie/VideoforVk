package akhmedoff.usman.thevt.ui.favourites

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import androidx.lifecycle.Observer

class FavouritesPresenter(
        override var view: FavouritesContract.View?,
        private val videoRepository: VideoRepository
) : FavouritesContract.Presenter {

    override fun onCreated() = refresh()

    override fun refresh() {
        view?.let { view ->
            view.showLoading(true)
            videoRepository
                    .getFaveVideos()
                    .observe(view, Observer { pagedList ->
                        view.showLoading(false)
                        when {
                            pagedList != null && pagedList.size > 0 ->
                                view.showVideos(pagedList)
                            pagedList == null -> view.showError()
                            else -> view.showEmptyList()
                        }
                    })
        }
    }

    override fun onVideoClicked(item: Video) {
    }

    override fun onDestroyed() {
        view = null
    }
}