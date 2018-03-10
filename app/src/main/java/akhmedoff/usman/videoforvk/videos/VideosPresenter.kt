package akhmedoff.usman.videoforvk.videos

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer

class VideosPresenter(
    override var view: VideosContract.View?,
    private val videoRepository: VideoRepository
) : VideosContract.Presenter {

    override fun onCreated() {
        view?.let { view ->
            view.showLoading(true)
            videoRepository
                .getVideos(view.getOwnerId())
                .observe(view, Observer {
                    view.showLoading(false)
                    if (it != null && it.size > 0) view.showVideos(it)
                    else view.showEmptyList()
                })
        }
    }

    override fun onVideoClicked(item: Video) {
        view?.showVideo(item)
    }
}