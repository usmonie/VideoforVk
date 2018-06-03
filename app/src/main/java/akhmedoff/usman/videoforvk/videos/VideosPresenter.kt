package akhmedoff.usman.videoforvk.videos

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer

class VideosPresenter(
        override var view: VideosContract.View?,
        private val videoRepository: VideoRepository
) : VideosContract.Presenter {

    override fun onCreated() = refresh()

    override fun refresh() {
        view?.let { view ->
            view.showLoading(true)
            videoRepository
                    .getVideos(view.getOwnerId()?.toInt())
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